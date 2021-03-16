package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService
{

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id)
    {

        LOG.debug("Reading the reporting structure [{}]",id);

        Employee employee = employeeRepository.findByEmployeeId(id); // fetching the employee structure with specific ID

        ReportingStructure reportingStructure = new ReportingStructure();

        reportingStructure.setEmployee(employee); // set the filled out employee details into our reporting structure
        reportingStructure.setNumberOfReports(totalNumOfReports(employee)); //

        return reportingStructure;
    }

    public int totalNumOfReports(Employee employee)
    {
        int reportCount = 0;
        if(employee.getDirectReports()==null)
        {
            return reportCount;
        }
        else
            {
                int size = employee.getDirectReports().size(); // find the size of direct reports for given employee
                reportCount += size; //  store the number of direct reports

                for (Employee current: employee.getDirectReports()) // this loop will get each employee from direct Reports
                {
                    Employee employee1 =  employeeRepository.findByEmployeeId(current.getEmployeeId()); // this will get the employee details of each employee present in direct reports
                    reportCount += totalNumOfReports(employee1);

                    /* recursion call to check if the direct report employee has its own set of direct report employees,
                    in this way we can  calculate the total number of distinct reports */
                }

            }
        return reportCount;
    }
}
