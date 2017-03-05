package bltip.model;

public class Tip {

    private int homeTip;

    private int guestTip;

    private TipType tipType;

    public Tip(int homeTip, int awayTip, TipType tipType) {
        this.homeTip = homeTip;
        this.guestTip = awayTip;
        this.tipType = tipType;
    }

    public int getHomeTip() {
        return homeTip;
    }

    public int getGuestTip() {
        return guestTip;
    }

    public TipType getTipType() {
        return tipType;
    }

    public enum TipType {
        NORMAL,
        JOKER,
        DELUXE_JOKER;

        public static TipType of(boolean joker, boolean deluxeJoker) {
            TipType result = NORMAL;
            if (deluxeJoker) {
                result = DELUXE_JOKER;
            } else if (joker) {
                result = JOKER;
            }
            return result;
        }
    }
}
