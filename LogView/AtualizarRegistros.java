package LogView;
import java.util.TimerTask;

import Database.DatabaseHandler;

public class AtualizarRegistros extends TimerTask {

    @Override
    public void run() {
        try {
            if (LogView.sp.getViewport().getViewRect().getY() < 50) {
                System.out.println("Atualizando");
                LogView.t.setRegisterList(DatabaseHandler.getInstance().getAllRegisters());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
