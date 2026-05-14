package util;

import constant.ConstantValues;
import exception.ExceptionModel;
import exception.MissingMandatoryField;
import model.Item;

import javax.servlet.http.HttpServletRequest;

public class ItemRequestValidator {


    public static void validatePostItemRequest(Item item) {
        if (item != null
                && item.getUserId() != null
                && item.getName() != null
                && item.getPrice() != null
                && item.getTotalNumber() != null
                && item.getDeleted() != null
        ) {
            return;
        } else {
            throw new MissingMandatoryField(new ExceptionModel(
                    "Missing Mandatory field within POST /item request body",
                    404
            ));
        }
    }

    public static void validateUpdateItemRequest(HttpServletRequest request) {
        if (request.getParameter(ConstantValues.ID_PARAM) != null
                && request.getParameter(ConstantValues.NAME_PARAM) != null
                && request.getParameter(ConstantValues.PRICE_PARAM) != null
                && request.getParameter(ConstantValues.TOTAL_NUMBER_PARAM) != null
        ) {
            return;
        } else {
            throw new MissingMandatoryField(new ExceptionModel(
                    "Missing Mandatory field within PUT /item request body",
                    404
            ));
        }
    }

    public static void validateRemoveItemRequest(HttpServletRequest request) {

    }
}
