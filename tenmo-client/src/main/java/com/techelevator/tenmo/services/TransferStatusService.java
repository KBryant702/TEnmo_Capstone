package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusService {

    TransferStatus[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser);

    TransferStatus getTransferStatus(AuthenticatedUser authenticatedUser, String description);

    TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, long transferStatusId);

    void updateTransferStatus(AuthenticatedUser authenticatedUser, Transfer transfer);
}
