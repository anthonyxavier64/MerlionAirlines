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
public class ScheduleManagerMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public ScheduleManagerMenu() {
    }

    public ScheduleManagerMenu(Employee employee) {
        this.employee = employee;
    }

    public void run() {
    }

}
