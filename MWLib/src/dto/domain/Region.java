/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.List;

/**
 *
 * @author root
 */
public class Region extends PersistedDTO {

    public String name;
    //extrinsic
    public List<Town> towns;

    public Region(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public List<Town> getTowns() {
        return towns;
    }

}
