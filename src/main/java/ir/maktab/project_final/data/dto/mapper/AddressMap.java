package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.AddressDto;
import ir.maktab.project_final.data.entity.order.Address;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component

public class AddressMap {


    public Address createAddress(AddressDto addressDto) {
        Address address = Address.builder()
                .city(addressDto.getCity())
                .plaque(addressDto.getPlaque())
                .street(addressDto.getStreet()).build();
        return address;
    }

    public AddressDto createAddressDto(Address address) {
        AddressDto addressDto = AddressDto.builder()
                .street(address.getStreet())
                .plaque(address.getPlaque())
                .city(address.getCity()).build();
        return addressDto;
    }
}
