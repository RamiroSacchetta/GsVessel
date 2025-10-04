package com.example.GSVessel.Mapper;

import com.example.GSVessel.DTO.CrewMemberDTO;
import com.example.GSVessel.Model.CrewMember;
import com.example.GSVessel.Model.Ship;

public class CrewMemberMapper {

    public static CrewMember toEntity(CrewMemberDTO dto, Ship ship) {
        CrewMember crew = new CrewMember();
        crew.setId(dto.getId());
        crew.setName(dto.getName());
        crew.setRole(dto.getRole());
        crew.setContact(dto.getContact());
        crew.setShip(ship);
        return crew;
    }

    public static CrewMemberDTO toDTO(CrewMember crew) {
        return new CrewMemberDTO(
                crew.getId(),
                crew.getName(),
                crew.getRole(),
                crew.getContact(),
                crew.getShip() != null ? crew.getShip().getId() : null
        );
    }
}
