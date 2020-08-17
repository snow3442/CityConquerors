
package model.passives;
import controller.GameController;
import controller.GridLogicController;

public interface PassiveInterface {
   public final PassiveHelper passiveHelper = new PassiveHelper();
   public final GridLogicController gridLogicController = new GridLogicController();
}
