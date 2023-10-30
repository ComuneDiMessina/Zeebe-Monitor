package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class FileDTO {
	
    protected String filename;
    protected String mimeType;
    protected byte[] content;

}
