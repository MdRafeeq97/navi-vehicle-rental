package org.example.service;

import org.example.enums.VehicleType;
import org.example.models.Branch;
import org.example.utils.CommonUtils;

import java.util.*;

public class BranchManager {
    private final Map<String, Branch> branchMap = new HashMap<>();

    /***
     * Add a branch to DB/memory store
     * @param branchId
     * @param vehicleTypes
     * @return
     */
    public boolean addBranch(String branchId, String[] vehicleTypes) {
        //check if branch with given Id already exists
        if(branchMap.containsKey(branchId)) {
            return false;
        }

        Branch branch = new Branch();
        branch.setId(branchId);
        List<VehicleType> supportedVehicleTypes = new ArrayList<>();
        for(String vehicleType : vehicleTypes) {
            VehicleType supportedVehicleType = CommonUtils.parseVehicleType(vehicleType);
            if(Objects.isNull(supportedVehicleType)) {
                return false;
            }
            supportedVehicleTypes.add(supportedVehicleType);
        }

        branch.setSupportedVehicles(supportedVehicleTypes);
        branchMap.put(branchId, branch);
        return true;
    }

    public Branch getBranch(String branchId) {
        return branchMap.get(branchId);
    }
}
