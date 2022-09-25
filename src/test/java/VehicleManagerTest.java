import org.example.enums.VehicleType;
import org.example.models.Branch;
import org.example.models.Car;
import org.example.models.Slot;
import org.example.models.Vehicle;
import org.example.service.BranchManager;
import org.example.service.VehicleManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleManagerTest {
    @Mock
    BranchManager branchManager;

    @InjectMocks
    VehicleManager vehicleManager;

    @Test
    void testAddVehicleSuccess() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));
        when(branchManager.getBranch(any())).thenReturn(branch);
        boolean res = vehicleManager.addVehicle("B1", "CAR", "V1", 1000);
        assertTrue(res);
    }

    @Test
    void testAddVehicleBranchNotFound() {
        when(branchManager.getBranch(any())).thenReturn(null);
        boolean res = vehicleManager.addVehicle("B1", "CAR", "V1", 1000);
        assertFalse(res);
    }

    @Test
    void testAddVehicleUnsupportedVehicleType() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));
        when(branchManager.getBranch(any())).thenReturn(branch);
        boolean res = vehicleManager.addVehicle("B1", "VAN", "V1", 1000);
        assertFalse(res);
    }

    @Test
    void testAddVehicleWithIdAlreadyExists() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));

        Vehicle vehicle = new Car();
        vehicle.setVehicleId("V1");
        branch.setVehicles(Arrays.asList(vehicle));
        when(branchManager.getBranch(any())).thenReturn(branch);
        boolean res = vehicleManager.addVehicle("B1", "CAR", "V1", 1000);
        assertFalse(res);
    }

    @Test
    void bookVehicleSuccess() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));

        Vehicle vehicle = new Car();
        vehicle.setVehicleId("V1");
        vehicle.setPrice(400);
        vehicle.setVehicleType(VehicleType.CAR);

        Vehicle vehicle2 = new Car();
        vehicle2.setVehicleId("V2");
        vehicle2.setPrice(500);
        vehicle2.setVehicleType(VehicleType.CAR);

        branch.setVehicles(Arrays.asList(vehicle, vehicle2));
        when(branchManager.getBranch(any())).thenReturn(branch);
        Integer totalPrice = vehicleManager.bookVehicle("B1", "CAR", 2, 4);
        assertEquals(800, totalPrice);

    }

    @Test
    void bookVehicleWithNoVehiclesInBranch() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));

        branch.setVehicles(Collections.emptyList());
        when(branchManager.getBranch(any())).thenReturn(branch);
        Integer totalPrice = vehicleManager.bookVehicle("B1", "CAR", 2, 4);
        assertEquals(-1, totalPrice);
    }

    @Test
    void bookVehicleSortAlreadyBooked() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));

        Vehicle vehicle = new Car();
        vehicle.setVehicleId("V1");
        vehicle.setPrice(400);
        vehicle.setVehicleType(VehicleType.CAR);

        branch.setVehicles(Arrays.asList(vehicle));
        when(branchManager.getBranch(any())).thenReturn(branch);
        Integer totalPrice1 = vehicleManager.bookVehicle("B1", "CAR", 2, 4);
        Integer totalPrice2 = vehicleManager.bookVehicle("B1", "CAR", 2, 4);

        assertEquals(800, totalPrice1);
        assertEquals(-1, totalPrice2);

    }

    @Test
    void displayVehicles() {
        Branch branch = new Branch();
        branch.setId("B1");
        branch.setSupportedVehicles(Arrays.asList(VehicleType.CAR, VehicleType.BIKE));

        Vehicle vehicle = new Car();
        vehicle.setVehicleId("V1");
        vehicle.setPrice(400);
        vehicle.setVehicleType(VehicleType.CAR);

        Slot slot = new Slot();
        slot.setStartTime(2);
        slot.setEndTime(3);

        vehicle.setSlots(Arrays.asList(slot));
        branch.setVehicles(Arrays.asList(vehicle));
        when(branchManager.getBranch(any())).thenReturn(branch);
        List<String> vehicles = vehicleManager.displayVehicles("B1", 3, 4);
        assertEquals(1, vehicles.size());
        assertEquals("V1", vehicles.get(0));
    }
}
