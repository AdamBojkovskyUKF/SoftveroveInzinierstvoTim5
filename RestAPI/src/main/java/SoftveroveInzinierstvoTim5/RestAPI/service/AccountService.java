package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.AccountDTO;

public interface AccountService {
    AccountDTO saveAccount(AccountDTO account);
    boolean deleteAccount(final Integer id_account);
    List<AccountDTO> getAllAccounts();
    AccountDTO getAccountId(final Integer id_account);
}
