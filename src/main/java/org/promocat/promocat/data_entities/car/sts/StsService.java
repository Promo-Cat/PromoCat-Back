package org.promocat.promocat.data_entities.car.sts;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.car.CarService;
import org.promocat.promocat.data_entities.car.car_photo.CarPhoto;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.FileDTO;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Slf4j
public class StsService {

    private final StsRepository stsRepository;
    private final CarService carService;
    private final FileMapper fileMapper;

    public StsService(final StsRepository stsRepository,
                      final CarService carService,
                      final FileMapper fileMapper) {
        this.stsRepository = stsRepository;
        this.carService = carService;
        this.fileMapper = fileMapper;
    }

    /**
     * Сохраняет данный файл (фото автомобиля) в бд
     * @param file Файл, отправленный пользователем
     * @param carId Идентификатор машины, для которой загружено фото автомобиля
     * @return идентификатор файла (фото автомобиля) в бд
     */
    public Long upload(MultipartFile file, Long carId)  {
        Sts sts = new Sts();
        try {
            sts.setFile(new SerialBlob(file.getBytes()));
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some problems with getting file");
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some problems with converting file");
        }
        sts.setDataType(file.getContentType());
        sts.setFileName(file.getOriginalFilename());
        return stsRepository.save(sts).getId();
    }

    public FileDTO findByCarId(Long carId) {
        CarDTO carDTO = carService.findById(carId);
        Optional<Sts> stsOptional = stsRepository.findById(carDTO.getStsId());
        if (stsOptional.isEmpty()) {
            throw new ApiCarNotFoundException("Sts for car with id " + carId + "doesn`t exist");
        }
        return fileMapper.toDto(stsOptional.get());
    }
}
