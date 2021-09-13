package org.qiqiang.forest.example.mvc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author qiqiang
 */
@ToString
@Getter
@Setter
public class TimeTestVO {
    private Date date;
    private LocalDate localDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;

    private XssTestVO xssTestVO;

    private List<String> list;
}
