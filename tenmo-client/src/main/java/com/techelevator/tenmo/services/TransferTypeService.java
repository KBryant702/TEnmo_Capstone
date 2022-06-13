package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

public interface TransferTypeService {

    Transfer getTransferType(AuthenticatedUser authenticatedUser, String description);

    Transfer getTransferTypeById(AuthenticatedUser authenticatedUser, long transferTypeId);
}
