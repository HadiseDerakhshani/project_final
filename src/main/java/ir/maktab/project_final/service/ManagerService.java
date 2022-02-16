package ir.maktab.project_final.service;

import ir.maktab.project_final.data.entity.user.Manager;


public interface ManagerService {


    Manager createManager(String userName, String pass);

    void Save(Manager manager);

    Manager checkManager(Manager manager);

    void customerConfirmation();

    void payment(int number, double amount, int score);
}
