package com.repairagency.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tag that intended to format LocalDateTime according to given pattern.
 * It is needed because standard fmt:formatDate tag does not support LocalDateTime
 */

public class DateFormatter extends SimpleTagSupport {

    private static final Logger logger = LogManager.getLogger();

    private LocalDateTime dateTime;
    private String pattern;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (dateTime == null) {
            return;
        }
        try {
            String date = dateTime.format(DateTimeFormatter.ofPattern(pattern));
            getJspContext().getOut().print(date);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid date format pattern", ex);
            throw new JspException(ex);
        }
    }

}
