package com.sastix.licenser.server.utils;

import java.util.function.Function;

public interface Conversions {
    /**
     * Example:
     *
     * Function<AccessCode, AccessCodeDTO> convertAccessCodeToDTO = (a) -> {
         final AccessCodeDTO accessCodeDTO = new AccessCodeDTO();
         accessCodeDTO.setId(a.getId());
         accessCodeDTO.setDataType(a.getDataType());
         accessCodeDTO.setId(a.getId());
         return accessCodeDTO;
         };
     *
     * */

}
