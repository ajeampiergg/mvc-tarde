package pe.edu.upao.mvctarde.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import pe.edu.upao.mvctarde.models.Car;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upao.mvctarde.services.ParkingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;


    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars(){

        return ResponseEntity.ok(this.parkingService.getAllCars());
    }

    @GetMapping("/cars/{licensePlate}")
    public ResponseEntity<Car> getCarByLicensePlate(@PathVariable String licensePlate){
        Optional<Car> optionalCar = this.parkingService.getCarByLicensePlate(licensePlate);
        if(optionalCar.isPresent()){
            return ResponseEntity.ok(optionalCar.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCarByColor(@RequestParam(required = false) String color){
        if(color != null){
            List<Car> carsByColor = this.parkingService.getCarsByColor(color);
            return  ResponseEntity.ok(carsByColor);
        }
        return ResponseEntity.ok(this.parkingService.getAllCars());
    }


    @PostMapping("/cars")
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        this.parkingService.addCar(car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @DeleteMapping("/cars/{licensePlate}")
    public ResponseEntity<Void> removeCarByLicensePlate(@PathVariable String licensePlate){
        boolean removed = this.parkingService.removeCarByLicensePlate(licensePlate);
        if(removed){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cars/{licensePlate}/entry")
    public ResponseEntity<Void> registerEntry(@PathVariable String licensePlate){
        Car car = new Car(licensePlate,null);
        this.parkingService.parkCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/cars/{licensePlate}/exit")
    public ResponseEntity<Double> registerExit(@PathVariable String licensePlate){
        double fee = this.parkingService.calculateParkingFee(licensePlate);
        this.parkingService.unparkCar(licensePlate);
        return ResponseEntity.ok(fee);
    }

}
