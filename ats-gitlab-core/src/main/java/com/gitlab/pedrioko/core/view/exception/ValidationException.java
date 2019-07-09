package com.gitlab.pedrioko.core.view.exception;

import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ZKUtil;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        ZKUtil.showMessage(message, MessageType.ERROR);
    }

    public ValidationException() {
    }

}
