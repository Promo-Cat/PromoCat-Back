package org.promocat.promocat.data_entities.car.car_photo;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car.CarService;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.FileDTO;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Slf4j
public class CarPhotoService {

    private final CarPhotoRepository carPhotoRepository;
    private final FileMapper fileMapper;
    private final CarService carService;

    public CarPhotoService(final CarPhotoRepository carPhotoRepository,
                           final FileMapper fileMapper,
                           final CarService carService) {
        this.carPhotoRepository = carPhotoRepository;
        this.fileMapper = fileMapper;
        this.carService = carService;
    }

    /**
     * Сохраняет данный файл (фото автомобиля) в бд
     * @param file Файл, отправленный пользователем
     * @param carId Идентификатор машины, для которой загружено фото автомобиля
     * @return идентификатор файла (фото автомобиля) в бд
     */
    public Long upload(MultipartFile file, Long carId)  {
        CarPhoto carPhoto = new CarPhoto();
        try {
            carPhoto.setFile(new SerialBlob(file.getBytes()));
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some problems with getting file");
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some problems with converting file");
        }
        carPhoto.setDataType(file.getContentType());
        carPhoto.setFileName(file.getOriginalFilename());
        return carPhotoRepository.save(carPhoto).getId();
    }

    public FileDTO findByCarId(Long carId) {
        CarDTO carDTO = carService.findById(carId);
        Optional<CarPhoto> carPhotoOptional = carPhotoRepository.findById(carDTO.getPhotoId());
        if (carPhotoOptional.isEmpty()) {
            throw new ApiCarNotFoundException("Car with id " + carId + "doesn`t exist");
        }
        return fileMapper.toDto(carPhotoOptional.get());
    }

}
