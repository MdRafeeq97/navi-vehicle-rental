import org.example.models.Branch;
import org.example.service.BranchManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BranchManagerTest {
    @InjectMocks
    BranchManager branchManager;

    @Test
    void testAddBranchSuccess() {
        String[] vehicleTypes = new String[]{"CAR", "VAN"};
        boolean res = branchManager.addBranch("B1", vehicleTypes);
        assertTrue(res);
    }

    @Test
    void testAddBranchUnsupportedVehicleType() {
        String[] vehicleTypes = new String[]{"CAR", "VAN", "BUS"};
        boolean res = branchManager.addBranch("B1", vehicleTypes);
        assertFalse(res);
    }

    @Test
    void testGetBranchSuccess() {
        Branch branch = branchManager.getBranch("B1");
        assertNull(branch);
    }
}
