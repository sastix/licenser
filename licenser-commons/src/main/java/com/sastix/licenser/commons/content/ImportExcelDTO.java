package com.sastix.licenser.commons.content;

import javax.validation.constraints.NotNull;

public class ImportExcelDTO {

    @NotNull
    private byte[] excelFile;

    @NotNull
    private int tenantId;

    public ImportExcelDTO(){
    }

    public ImportExcelDTO(byte[] excelFile, int tenantId){
        this.excelFile = excelFile;
        this.tenantId = tenantId;
    }

    public byte[]  getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(byte[]  excelFile) {
        this.excelFile = excelFile;
    }

    public int getTenantId() {
        return tenantId;
    }
}
