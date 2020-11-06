/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import entity.Employee;
import java.util.Scanner;

/**
 *
 * @author Antho
 */
public class AdministratorMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public AdministratorMenu() {
    }

    public AdministratorMenu(Employee employee) {
        this.employee = employee;
    }

    public void run() {

    }

}
