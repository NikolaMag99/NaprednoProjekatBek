package raf.rs.NwpNikolaDomaci3.services;

import raf.rs.NwpNikolaDomaci3.model.ErrorMessage;
import raf.rs.NwpNikolaDomaci3.model.User;

import java.util.List;

public interface ErrorMessageInterfaceService extends IService<ErrorMessage, Long> {
    List<ErrorMessage> findErrorMessagesForUser(User user);
}
