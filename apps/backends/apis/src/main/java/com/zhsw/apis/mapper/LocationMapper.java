package com.zhsw.apis.mapper;

import com.zhsw.apis.model.ExternalLocation;
import com.zhsw.apis.model.ExternalLocationResult;
import lombok.Data;
import org.openapitools.model.Location;
import org.openapitools.model.Locations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class LocationMapper {

    public Location mapExternalLocationToLocation(ExternalLocation externalLocation) {
        return new Location(
                externalLocation.getPostalCode(),
                externalLocation.getName(),
                externalLocation.getCanton().getShortName());
    }

    public Locations mapExternalLocationResultToLocationList(ExternalLocationResult externalLocationResult) {
        List<Location> locations = externalLocationResult.getLocations().stream()
                .map(this::mapExternalLocationToLocation)
                .toList();
        return new Locations(locations);
    }
}
