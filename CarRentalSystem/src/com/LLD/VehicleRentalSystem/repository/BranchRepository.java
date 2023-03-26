package com.LLD.VehicleRentalSystem.repository;

import com.LLD.VehicleRentalSystem.models.Branches;

import java.util.List;

public class BranchRepository {
    public List<Branches> branchesList;

    public void addBranch(Branches branches) {
        branchesList.add(branches);
    }

    public BranchRepository(List<Branches> branchesList) {
        this.branchesList = branchesList;
    }

    public List<Branches> getBranchesList() {
        return branchesList;
    }

    public void setBranchesList(List<Branches> branchesList) {
        this.branchesList = branchesList;
    }

    public Branches getBranchByName(String branchName) {
        for (Branches branch: branchesList) {
            if (branch.getBranchName() == branchName) {
                return branch;
            }
        }
        return null;
    }
}
