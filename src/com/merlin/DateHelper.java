     package com.merlin;
 
 import java.text.DecimalFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import org.joda.time.DateTime;
 import org.joda.time.Days;
 import org.joda.time.Period;
 import org.joda.time.PeriodType;
 import org.joda.time.ReadableInstant;
 
 
 
 
 
 
 
 
 
 
 public class DateHelper
 {
   public Calendar rightNow = Calendar.getInstance();
   public SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   private SimpleDateFormat dateSaveFormatter = new SimpleDateFormat("yyyyMMdd");
   private SimpleDateFormat timeSaveFormatter = new SimpleDateFormat("HHmm");
   private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
   private Date givenDate;
   private String inputDate;
   
   public String getCurrentYear() {
/*  37 */     return this.yearFormatter.format(Calendar.getInstance().getTime());
   }
   
   public int getLastDateOfCurrentMonth() {
     Calendar c = Calendar.getInstance();
     return c.getActualMaximum(5);
            }

    public String getLastDateOfMonth(String month, String year) throws ParseException {
        String givenDate = year.concat("-").concat(month).concat("-01");
        Calendar c = Calendar.getInstance();
        c.setTime(formatter.parse(givenDate));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return year.concat("-").concat(month).concat(Integer.toString(c.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }
   
   public String getMonthDifference(Date currentDate, Date maturityDate) {
/*  46 */     DecimalFormat integerFormatter = new DecimalFormat("#0");
/*  47 */     PeriodType monthDay = PeriodType.yearMonthDayTime().withYearsRemoved();
/*  48 */     Period difference = new Period((ReadableInstant)new DateTime(maturityDate), (ReadableInstant)new DateTime(currentDate), monthDay);
/*  49 */     return integerFormatter.format(difference.getMonths());
   }
 
  public int getDayDifference(Date currentDate, Date maturityDate) {
/*  80 */     int days = Days.daysBetween((ReadableInstant)new DateTime(maturityDate), (ReadableInstant)new DateTime(currentDate)).getDays();
    /*  96 */ return days;
   }
   
   public Integer getMonth(Date gDate) {
/* 100 */     int mo = 0;
/* 101 */     Calendar get = Calendar.getInstance();
/* 102 */     get.setTime(gDate);
/* 103 */     mo = get.get(2) + 1;
/* 104 */     return Integer.valueOf(mo);
   }
   
   public Integer getDay(Date gDate) {
/* 108 */     int mo = 0;
/* 109 */     Calendar get = Calendar.getInstance();
/* 110 */     get.setTime(gDate);
/* 111 */     mo = get.get(5);
/* 112 */     return Integer.valueOf(mo);
   }
   
   public Integer getYear(Date gDate) {
/* 116 */     int mo = 0;
/* 117 */     Calendar get = Calendar.getInstance();
/* 118 */     get.setTime(gDate);
/* 119 */     mo = get.get(1);
/* 120 */     return Integer.valueOf(mo);
   }
   public String convertDate(Date gvnDate) {
/* 123 */     Calendar get = Calendar.getInstance();
/* 124 */     get.setTime(gvnDate);
/* 125 */     return this.formatter.format(get);
   }
 
   
   public String getSavingDate() {
/* 130 */     return this.dateSaveFormatter.format(this.rightNow.getTime());
   }
   
   public String getSavingTime() {
/* 134 */     return this.timeSaveFormatter.format(this.rightNow.getTime());
   }
   
   public String getDateTime() {
/* 138 */     return this.dateTimeFormatter.format(this.rightNow.getTime());
   }
   
   public String getCurrentDate() {
/* 142 */     return this.formatter.format(this.rightNow.getTime());
   }
 
 
   
   public Date now(Calendar rightNow) {
/* 148 */     this.rightNow = rightNow;
/* 149 */     return rightNow.getTime();
   }
   
   public Date oneMonth(Calendar rightNow) {
/* 153 */     this.rightNow = rightNow;
/* 154 */     rightNow.add(2, 1);
/* 155 */     return rightNow.getTime();
   }
 
   
   public String oneMonth(String trans_date) {
/* 160 */     Calendar givenNow = Calendar.getInstance();
/* 161 */     givenNow.setTime(formatStringToDate(trans_date));
/* 162 */     givenNow.add(2, 1);
/* 163 */     return formatDate(givenNow.getTime());
   }
   
   public Date fourMonths(Calendar rightNow) {
/* 167 */     this.rightNow = rightNow;
/* 168 */     rightNow.add(2, 4);
/* 169 */     return rightNow.getTime();
   }
 
   
   public Date threeMonths(Calendar rightNow) {
/* 174 */     this.rightNow = rightNow;
/* 175 */     rightNow.add(2, 3);
/* 176 */     return rightNow.getTime();
   }
 
   
   public String fourMonths(String trans_date) {
/* 181 */     Calendar givenNow = Calendar.getInstance();
/* 182 */     givenNow.setTime(formatStringToDate(trans_date));
/* 183 */     givenNow.add(2, 4);
/* 184 */     return formatDate(givenNow.getTime());
   }
   
   public Date lessThreeMonths(Calendar rightNow) {
/* 188 */     this.rightNow = rightNow;
/* 189 */     rightNow.add(2, -3);
/* 190 */     return rightNow.getTime();
   }
   
   public Date lessFourMonths(Date trans_date) {
/* 194 */     Calendar cal = Calendar.getInstance();
/* 195 */     cal.setTime(trans_date);
/* 196 */     cal.add(2, -4);
/* 197 */     return cal.getTime();
   }
   
   public String formatDate(Date givenDate) {
/* 201 */     this.givenDate = givenDate;
/* 202 */     return this.formatter.format(givenDate);
   }
 
    
   
   public Date formatStringToDate(String maturityS) {
/* 207 */     Date maturityD = null;
     try {
/* 209 */       maturityD = this.formatter.parse(maturityS);
/* 210 */     } catch (ParseException ex) {
/* 211 */       (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
/* 212 */       Logger.getLogger(DateHelper.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
/* 214 */     return maturityD;
            }

     public Calendar DateToCalendar(Date date) {
/* 235 */     Calendar cal = Calendar.getInstance();
/* 236 */     cal.setTime(date);
/* 237 */     return cal;
   }
     
     private Double getFactorial(Integer num) {
            Double f = 1.0;
            for (int i = 1; i <= num; i++) {
                
                f *= f;
            }
            return f;
        }
      
   public static void main(String[] args) {
///* 241 */     Sangla loan = new Sangla();
///* 242 */     loan.setItem_code_a("8656TA");
///* 243 */     loan.retrieveSangla("8656TA");
///* 244 */     loan.setItemCodes();
///* 245 */     DateHelper dateHelp = new DateHelper();
//    System.out.println(dateHelp.lessThreeMonths(Calendar.getInstance()));
///* 246 */     dateHelp.getMonthDifference(dateHelp.formatStringToDate("2015-05-31"), dateHelp.formatStringToDate("2015-05-01"));
///* 247 */     System.out.println("MONTH difference = " + dateHelp.getMonthDifference(dateHelp.formatStringToDate("2015-05-31"), dateHelp.formatStringToDate("2015-05-01")));
///* 248 */     System.out.println(loan.getExpiration_date() + "    " + dateHelp.formatStringToDate(loan.getExpiration_date()));
///* 249 */     System.out.println(Calendar.getInstance().getTime());
///* 250 */     System.out.println("DAY difference = " + dateHelp.getDayDifference(Calendar.getInstance().getTime(), dateHelp.formatStringToDate(loan.getExpiration_date())));
        int number = 20;
        DateHelper dateHelp = new DateHelper();
        Thread newThr = new Thread(() -> {
            System.out.println("Factorial of " + number + " is: " + dateHelp.getFactorial(number));
        });       
        newThr.start();
        if (!newThr.isAlive()) {
            System.out.println("outside thread");
        }
//        ExecutorService threadpool = Executors.newCachedThreadPool();
//Future<Long> futureTask = (Future<Long>) threadpool.submit(() -> dateHelp.getFactorial(number));
//
//while (!futureTask.isDone()) {
//    System.out.println("FutureTask is not finished yet..."); 
//} 
//long result = futureTask.get(); 
//
//threadpool.shutdown();
        
        
   }
 }