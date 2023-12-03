package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.AccountDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Account;
import SoftveroveInzinierstvoTim5.RestAPI.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Configurable
public class DefaultAccountService implements AccountService{

    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDTO saveAccount(AccountDTO account) {
        Account accountModel = populateAccountEntity(account);
        return populateAccountData(accountRepository.save(accountModel));
    }

    @Override
    public boolean deleteAccount(Integer id_account) {
        accountRepository.deleteById(id_account);
        return true;
    }

    @Override
    public List<AccountDTO> getAllAccounts() {

        List < AccountDTO > accounts = new ArrayList < > ();
            Iterable < Account > accountList =  accountRepository.findAll();
            accountList.forEach(account -> {
                accounts.add(populateAccountData(account));
            });
            return accounts;
    }

    @Override
    public AccountDTO getAccountId(Integer id_account) {
        return populateAccountData(accountRepository.findById(id_account).orElseThrow(() -> new EntityNotFoundException("Account not found")));
    }
    
    private AccountDTO populateAccountData(final Account account) {
        AccountDTO accountData = new AccountDTO();
        accountData.setId_account(account.getId_account());
        accountData.setCompany_id_company(account.getCompany_id_company());
        accountData.setEmail_address(account.getEmail_address());
        accountData.setInstitute(account.getInstitute());
        accountData.setPassword(account.getPassword());
        accountData.setPerson_id_person(account.getPerson_id_person());
        accountData.setRole(account.getRole());
        accountData.setSignup_year(account.getSignup_year());
        accountData.setStudy_program_idstudy_program(account.getStudy_program_idstudy_program());
        return accountData;
    }

    private Account populateAccountEntity(AccountDTO accountData) {
        Account account = new Account();
        account.setId_account(accountData.getId_account());
        account.setCompany_id_company(accountData.getCompany_id_company());
        account.setEmail_address(accountData.getEmail_address());
        account.setInstitute(accountData.getInstitute());
        account.setPassword(accountData.getPassword());
        account.setPerson_id_person(accountData.getPerson_id_person());
        account.setRole(accountData.getRole());
        account.setSignup_year(accountData.getSignup_year());
        account.setStudy_program_idstudy_program(accountData.getStudy_program_idstudy_program());
        return account;
    }

}
