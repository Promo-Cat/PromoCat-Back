package org.promocat.promocat.data_entities.stock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.CompanyStatus;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.company.CompanyService;
import org.promocat.promocat.data_entities.stock.csvFile.CSVFileService;
import org.promocat.promocat.data_entities.stock.poster.PosterService;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.security.ApiForbiddenException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.MimeTypes;
import org.promocat.promocat.utils.MultiPartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.STOCK})
public class StockController {

    private final StockService stockService;
    private final CompanyService companyService;
    private final PosterService posterService;
    private final MultiPartFileUtils multiPartFileUtils;
    private final CSVFileService csvFileService;

    @Autowired
    public StockController(final StockService stockService,
                           final CompanyService companyService,
                           final PosterService posterService,
                           final MultiPartFileUtils multiPartFileUtils,
                           final CSVFileService csvFileService) {
        this.stockService = stockService;
        this.companyService = companyService;
        this.posterService = posterService;
        this.multiPartFileUtils = multiPartFileUtils;
        this.csvFileService = csvFileService;
    }

    @ApiOperation(value = "Create stock",
            notes = "Adds stock for company with id specified in request.",
            response = StockDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error", response = ApiValidationException.class),
            @ApiResponse(code = 415, message = "Not acceptable media type", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> addStock(@Valid @RequestBody StockDTO stock,
                                             @RequestHeader("token") String token) {
        CompanyDTO company = companyService.findByToken(token);
        StockDTO companyCurrentStock = company.getCurrentStockId() == 0L ? null : stockService.findById(company.getCurrentStockId());
        if (companyCurrentStock != null
                && companyCurrentStock.getStatus() != StockStatus.STOCK_IS_OVER_WITH_POSTPAY
                && companyCurrentStock.getStatus() != StockStatus.BAN) {
            // TODO: 18.07.2020 exception (previous stock isn`t ended)
            throw new RuntimeException("Previous stock isn`t ended. Unable to create new one");
        }
        if (company.getCompanyStatus() != CompanyStatus.FULL) {
            throw new RuntimeException("Company account isn`t fully filled");
        }
        stock.setCompanyId(company.getId());
        stock.setStatus(StockStatus.POSTER_NOT_CONFIRMED);
        StockDTO res = stockService.create(stock);
        company.setCurrentStockId(res.getId());
        companyService.save(company);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "Load poster",
            notes = "Loads new poster for this stock. Max size is 5MB, .pdf is required format",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "File format or size problems", response = ApiException.class),
            @ApiResponse(code = 403, message = "Not company`s stock", response = ApiException.class),
            @ApiResponse(code = 404, message = "Company not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Stock not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/api/company/stock/{id}/poster", method = RequestMethod.POST)
    public ResponseEntity<String> loadPoster(@PathVariable("id") Long id,
                                             @RequestParam("poster") MultipartFile file,
                                             @RequestHeader("token") String token) {

        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            StockDTO stock = stockService.findById(id);
            if (MimeTypes.MIME_APPLICATION_PDF.equals(file.getContentType())) {
                if (multiPartFileUtils.getSizeInMB(file.getSize()) <= 5) {
                    PosterDTO poster = posterService.loadPoster(file, stock.getPosterId());
                    stock.setPosterId(poster.getId());
                    stockService.save(stock);
                    return ResponseEntity.ok("{}");
                } else {
                    throw new ApiFileFormatException("File too large. Max size 5MB");
                }
            } else {
                throw new ApiFileFormatException(String.format(".pdf required %s provided", file.getContentType()));
            }
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    @ApiOperation(value = "Get poster",
            notes = "Get poster for this stock. Poster in .pdf format.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Not company`s stock", response = ApiException.class),
            @ApiResponse(code = 404, message = "Company not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Poster not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Stock not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/api/company/stock/{id}/poster", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPoster(@PathVariable("id") Long id,
                                              @RequestHeader("token") String token) {
        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            StockDTO stock = stockService.findById(id);
            PosterDTO poster = posterService.findById(stock.getPosterId());
            return posterService.getResourceResponseEntity(posterService.getPoster(poster));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    @ApiOperation(value = "Get posters preview",
            notes = "Get posters preview for this stock. Poster in .png format.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Not company`s stock", response = ApiException.class),
            @ApiResponse(code = 404, message = "Company not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Poster not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Stock not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/data/company/stock/{id}/poster/preview", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPosterPreview(@PathVariable("id") Long id,
                                                     @RequestHeader("token") String token) {
        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            StockDTO stock = stockService.findById(id);
            MultiPartFileDTO posterPreview = posterService.getPosterPreview(posterService.findById(stock.getPosterId()));
            return posterService.getResourceResponseEntity(posterPreview);
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    @ApiOperation(value = "Delete poster",
            notes = "Deletes poster and posters preview for this stock.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Not company`s stock", response = ApiException.class),
            @ApiResponse(code = 404, message = "Company not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Poster not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "Stock not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/api/company/stock/{id}/poster", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePoster(@PathVariable("id") final Long id,
                                               @RequestHeader("token") final String token) {
        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            StockDTO stockDTO = stockService.findById(id);
            posterService.delete(stockDTO.getPosterId());
            stockDTO.setPosterId(0L);
            stockService.save(stockDTO);
            return ResponseEntity.ok("{}");
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    @ApiOperation(value = "Get file",
            notes = "Get file for this stock. file in .csv format.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "File not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/admin/stock/csvFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> getCSVFile(@PathParam("name") final String name) {
        return csvFileService.getFile(name);
    }

    @ApiOperation(value = "Delete file",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "File not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/admin/stock/delete/csvFile/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFile(@PathVariable("name") String name) {
        CSVFileDTO file = csvFileService.findByName(name);
        csvFileService.delete(file.getId());
        return ResponseEntity.ok("{}");
    }

    // ------ Admin methods ------

//    @ApiOperation(value = "Generate promo-codes to stock.",
//            notes = "Returning stock with id specified in request",
//            response = StockDTO.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 404,
//                    message = "Stock not found",
//                    response = ApiException.class),
//            @ApiResponse(code = 406,
//                    message = "Some DB problems",
//                    response = ApiException.class)
//    })
//    @RequestMapping(path = "/admin/company/stock/generate/{id}", method = RequestMethod.POST)
//    public ResponseEntity<StockDTO> generate(@PathVariable("id") Long id) {
//        StockDTO stock = stockService.findById(id);
//        if (Objects.isNull(stock.getIsAlive())) {
//            companyService.verify(stock.getCompanyId());
//            return ResponseEntity.ok(promoCodeService.savePromoCodes(stockService.setActive(id, true)));
//        }
//        throw new ApiStockActivationStatusException(String.format(
//                "Stock with id: %d is already %s", id, stock.getIsAlive() ? "activated" : "deactivated"));
//    }

    @ApiOperation(value = "Set startTime stock",
            notes = "Setting startTIme for stock by id",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Stock not found", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server error", response = ApiException.class),
    })
    @RequestMapping(path = "/admin/stock/startTime/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> setStartTime(@PathVariable("id") final Long id,
                                               @RequestParam("time")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time) {
        StockDTO stock = stockService.findById(id);
        stock.setStartTime(time);
        stockService.save(stock);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Deactivate stock.",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/stock/active/{id}", method = RequestMethod.POST)
    public ResponseEntity<StockDTO> setActiveStock(@PathVariable("id") Long id,
                                                   @RequestParam("activation_status") String activationStatus) {
        return ResponseEntity.ok(stockService.setActive(id, StockStatus.valueOf(activationStatus.toUpperCase())));
    }


    @ApiOperation(value = "Get stock by id",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/stock/{id}", method = RequestMethod.GET)
    public ResponseEntity<StockDTO> getStockById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @ApiOperation(value = "Delete stock by id",
            notes = "Deleting stock, whose id specified in params",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStockById(@PathVariable("id") final Long id) {
        stockService.deleteById(id);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Get all inactive stocks",
            notes = "Getting all stocks, whose status not ACTIVE",
            response = StockDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/inactive", method = RequestMethod.GET)
    public ResponseEntity<List<StockDTO>> getAllInactiveStock() {
        return ResponseEntity.ok(stockService.getAllInactive());
    }

    @ApiOperation(value = "Get all stocks by status",
            notes = "Getting all stocks",
            response = StockDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/status", method = RequestMethod.GET)
    public ResponseEntity<List<StockDTO>> getStockByStatus(@RequestParam("status") StockStatus status) {
        return ResponseEntity.ok(stockService.getStockByStatus(status));
    }
}
