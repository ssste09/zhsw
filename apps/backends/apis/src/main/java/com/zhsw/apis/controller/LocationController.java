package com.zhsw.apis.controller;

import com.zhsw.apis.mapper.LocationMapper;
import com.zhsw.apis.service.LocationService;
import org.openapitools.api.LocationApi;
import org.openapitools.model.Locations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LocationController implements LocationApi {
    private final LocationMapper locationMapper;
    private final LocationService locationService;

    public LocationController(LocationMapper locationMapper, LocationService locationService) {
        this.locationMapper = locationMapper;
        this.locationService = locationService;
    }

    @Override
    public ResponseEntity<Locations> locationGET(String postalCode, String city) {
        if ((postalCode == null || postalCode.isBlank()) && (city == null || city.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either postalCode or city must be provided");
        }

        if (postalCode == null) {
            postalCode = "";

            return locationService
                    .getLocation(postalCode, city)
                    .map(locationMapper::mapExternalLocationResultToLocationList)
                    .map(ResponseEntity::ok)
                    .get();
        }

        if (city == null) {
            city = "";
        }

        return locationService
                .getLocation(postalCode, city)
                .map(locationMapper::mapExternalLocationResultToLocationList)
                .map(ResponseEntity::ok)
                .get();
    }
}
