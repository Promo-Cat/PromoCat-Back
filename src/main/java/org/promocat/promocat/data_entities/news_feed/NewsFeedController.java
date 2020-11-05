package org.promocat.promocat.data_entities.news_feed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.NewsFeedDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.utils.EntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Danil Lyskin at 21:13 31.10.2020
 */

@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.NEWS_FEED})
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @Autowired
    public NewsFeedController(final NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    @ApiOperation(value = "Add news",
            notes = "Returning news, which was added",
            response = NewsFeedDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/add/news", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsFeedDTO> addNews(@RequestBody NewsFeedDTO newsFeed) {
        newsFeed.setStartTime(LocalDateTime.now());
        return ResponseEntity.ok(newsFeedService.save(newsFeed));
    }

    @ApiOperation(value = "Update news",
            notes = "Returning news, which was updated",
            response = NewsFeedDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/update/news", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsFeedDTO> updateNews(@Valid @RequestBody NewsFeedDTO newsFeed) {
        NewsFeedDTO actualNewsFeed = newsFeedService.findById(newsFeed.getId());
        EntityUpdate.copyNonNullProperties(newsFeed, actualNewsFeed);
        return ResponseEntity.ok(newsFeedService.save(actualNewsFeed));
    }

    @ApiOperation(value = "Delete news by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/delete/news/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteNews(@PathVariable("id") final Long id) {
        newsFeedService.deleteById(id);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Get news by id",
            notes = "Returning news, which id specified in params",
            response = NewsFeedDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/id/news/{id}", method = RequestMethod.GET)
    public ResponseEntity<NewsFeedDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsFeedService.findById(id));
    }

    @ApiOperation(value = "Get news by type",
            notes = "Returning list of news, which type specified in params",
            response = NewsFeedDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/api/type/news/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<NewsFeedDTO>> getNewsByType(@PathVariable("type") String type) {
        return ResponseEntity.ok(newsFeedService.findByType(type));
    }
}
