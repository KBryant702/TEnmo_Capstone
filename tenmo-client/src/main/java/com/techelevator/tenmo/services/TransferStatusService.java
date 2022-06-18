package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

public interface TransferStatusService {

    Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser);

    Transfer getTransferStatus(AuthenticatedUser authenticatedUser, String description);

    Transfer getTransferStatusById(AuthenticatedUser authenticatedUser, long transferStatusId);

    boolean updateTransferStatus(AuthenticatedUser authenticatedUser, Transfer transfer);
}
