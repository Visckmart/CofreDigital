package LogView;
import java.util.TimerTask;

import Database.DatabaseHandler;

public class AtualizarRegistros extends TimerTask {

    @Override
    public void run() {
        try {
            LogView.t.setRegisterList(DatabaseHandler.getInstance().getAllRegisters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
