package mpis_mariadb_2;

public class Computation {

    private DecimalHelper decimalChanger = new DecimalHelper();

    private double principal = 0.0D;

    private double interestRate = 0.0D;

    private double interest = 0.0D;

    private double advanceInterest = 0.0D;

    private double advanceInterestRate = 0.0D;

    private double serviceCharge = 5.0D;

    private double insurance = 0.0D;

    private double otherCharges = 0.0D;

    private double totalAmountDue = 0.0D;

    private double partials = 0.0D;

    private double netIncrease = 0.0D;

    private double netDecrease = 0.0D;

    private double netProceeds = 0.0D;

    private double netDueOrProceeds = 0.0D;

    private double stamp = 0.0D;

    private double liq_dam = 0.0D;

    private double aff = 0.0D;

    private double unpaid = 0.0D;

    private int numberOfMonths = 0;

    private double totalBalance = 0.0D;

    public String computeInsurance(double principal) {
        this.insurance = Math.ceil(principal * 0.004D);
        return this.decimalChanger.FormatNumber(this.insurance);
    }

    public String computeInsuranceWithNetIncrease(double principal, double netIncrease) {
        this.insurance = Math.ceil((principal + netIncrease) * 0.004D);
        return this.decimalChanger.FormatNumber(this.insurance);
    }

    public String computeInsuranceWithNetDecrease(double principal, double netDecrease) {
        this.insurance = Math.ceil((principal - netDecrease) * 0.004D);
        return this.decimalChanger.FormatNumber(this.insurance);
    }

    public String computeInterest(double principal, double interestRate, int numberOfMonths) {
        this.interest = principal * interestRate / 100.0D * numberOfMonths;
        return this.decimalChanger.FormatNumber(this.interest);
    }

    public String computeInterestRate(double principal, double interest, int numberOfMonths) {
        this.interestRate = interest * 100.0D / principal * numberOfMonths;
        return this.decimalChanger.FormatPercent(this.interestRate);
    }

    public String computeAdvanceInterest(double principal, double advanceInterestRate) {
        this.advanceInterest = principal * advanceInterestRate / 100.0D;
        return this.decimalChanger.FormatNumber(this.advanceInterest);
    }

    public String computeAdvanceInterestWithNetIncrease(double principal, double netIncrease, double advanceInterestRate) {
        this.advanceInterest = (principal + netIncrease) * advanceInterestRate / 100.0D;
        return this.decimalChanger.FormatNumber(this.advanceInterest);
    }

    public String computeAdvanceInterestWithNetDecrease(double principal, double netDecrease, double advanceInterestRate) {
        this.advanceInterest = (principal - netDecrease) * advanceInterestRate / 100.0D;
        return this.decimalChanger.FormatNumber(this.advanceInterest);
    }

    public String computeAdvanceInterestRate(double principal, double advanceInterest) {
        this.advanceInterestRate = advanceInterest * 100.0D / principal;
        return this.decimalChanger.FormatPercent(this.advanceInterestRate);
    }

    public String computeAdvanceInterestRateWithNetIncrease(double principal, double netIncrease, double advanceInterest) {
        this.advanceInterestRate = Math.ceil(advanceInterest * 100.0D / (principal + netIncrease));
        return this.decimalChanger.FormatPercent(this.advanceInterestRate);
    }

    public String computeAdvanceInterestRateWithNetDecrease(double principal, double netDecrease, double advanceInterest) {
        this.advanceInterestRate = Math.ceil(advanceInterest * 100.0D / (principal - netDecrease));
        return this.decimalChanger.FormatPercent(this.advanceInterestRate);
    }

    public String computeAmountDue(double principal, double interestRate, int numberOfMonths, double advanceInterestRate, double serviceCharge, double otherCharges, double insurance, double partials) {
        this.totalAmountDue = principal * interestRate / 100.0D * numberOfMonths + principal * advanceInterestRate / 100.0D + serviceCharge + otherCharges + insurance - partials;
        return this.decimalChanger.FormatNumber(this.totalAmountDue);
    }

    public String computeAmountDueWithNetDecrease(double principal, double interestRate, int numberOfMonths, double netDecrease, double advanceInterestRate, double serviceCharge, double otherCharges, double insurance, double partials) {
        this.totalAmountDue = netDecrease + (principal - netDecrease) * advanceInterestRate / 100.0D + principal * interestRate / 100.0D * numberOfMonths + serviceCharge + otherCharges + insurance - partials;
        return this.decimalChanger.FormatNumber(this.totalAmountDue);
    }

    public String computeAmountDue(double interest, double serviceCharge, double otherCharges, double insurance, double partials) {
        this.totalAmountDue = interest + serviceCharge + otherCharges + insurance - partials;
        return this.decimalChanger.FormatNumber(this.totalAmountDue);
    }

    public String computeNetProceeds(double principal, double advanceInterestRate, double serviceCharge, double otherCharges, double insurance, double partials) {
        this.netProceeds = principal - principal * advanceInterestRate / 100.0D - serviceCharge - otherCharges - insurance - partials;
        return this.decimalChanger.FormatNumber(this.netProceeds);
    }

    public String computeNetProceeds(double principal, double advanceInterestRate, double serviceCharge, double insurance, double partials) {
        this.netProceeds = principal - principal * advanceInterestRate / 100.0D - serviceCharge - insurance - partials;
        return this.decimalChanger.FormatNumber(this.netProceeds);
    }

    public String computeNetDueOrProceeds(double principal, double interestRate, int numberOfMonths, double netIncrease, double advanceInterestRate, double serviceCharge, double otherCharges, double insurance, double partials) {
        this.totalAmountDue = principal + principal * interestRate / 100.0D * numberOfMonths + otherCharges + insurance;
        this.netProceeds = principal + netIncrease - (principal + netIncrease) * advanceInterestRate / 100.0D - serviceCharge - partials;
        this.netDueOrProceeds = this.netProceeds - this.totalAmountDue;
        return this.decimalChanger.FormatNumber(this.netDueOrProceeds);
    }

    public String computeNewPrincipal(double principal, double netIncrease, double netDecrease) {
        return this.decimalChanger.FormatNumber(principal + netIncrease - netDecrease);
    }

    public String computeTotalOutstandingBalance(double principal, double interestRate, int numberOfMonths, double otherCharges, double insurance, double partials) {
        this.totalBalance = principal + principal * interestRate / 100.0D * numberOfMonths + otherCharges + insurance - partials;
        return this.decimalChanger.FormatNumber(this.totalBalance);
    }

    public String computeTotalOutstandingBalance(double principal, double interest, double otherCharges, double insurance, double partials) {
        this.totalBalance = principal + interest + otherCharges + insurance - partials;
        return this.decimalChanger.FormatNumber(this.totalBalance);
    }

    public Double computeOtherCharges() {
        double oth = getLiq_dam() + getStamp() + getAff() + getUnpaid();
        return Double.valueOf(oth);
    }

    public double getInsurance() {
        return this.insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public double getStamp() {
        return this.stamp;
    }

    public void setStamp(double stamp) {
        this.stamp = stamp;
    }

    public double getLiq_dam() {
        return this.liq_dam;
    }

    public void setLiq_dam(double liq_dam) {
        this.liq_dam = liq_dam;
    }

    public double getAff() {
        return this.aff;
    }

    public void setAff(double aff) {
        this.aff = aff;
    }

    public double getUnpaid() {
        return this.unpaid;
    }

    public void setUnpaid(double unpaid) {
        this.unpaid = unpaid;
    }

    public double getPartials() {
        return this.partials;
    }

    public void setPartials(double partials) {
        this.partials = partials;
    }
}
