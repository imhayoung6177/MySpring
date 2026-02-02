package com.site.domain;

import lombok.Data;

@Data
public class File {
    private long id;
    private long board_id;
    private String original_name;
    private String stored_name;
    private String file_path;


}
