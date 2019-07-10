package Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateConverter {

    static Calendar now;
    static SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy 'в' HH:mm");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static SimpleDateFormat hoursAndMinutesFormat = new SimpleDateFormat("HH:mm");

    // Преобразование даты из int в строку для Сообщений
    public static String convertIntDateToString(int Date) {
        // Переводим в миллисекунды и long
        long newtime = ((long) Date) * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(newtime);
        // Получаем год, день, месяц, время, день недели
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        // День недели и месяц превращаем в слова через enum

        now = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        // Разница (в секундах) между текущей датой и датой в сообщении
        int delta = (int)((now.getTimeInMillis() - newtime) / 1000);

        String result = "хрень";
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int todayHour = calendar.get(Calendar.HOUR_OF_DAY);
        int todayMin = today.get(Calendar.MINUTE);

        if(todayYear == year && todayMonth == month && day < todayDay - 1) { // если больше 2 дней
            if (day == todayDay - 1) { // вчера
                // вывести (вчера часы:минуты)
                result = "вчера " + hoursAndMinutesFormat.format(newtime);
            } else if (day == todayDay && todayHour == hour) { // сегодня
                // вывести (часы:минуты)
                if (todayMin - minutes < 15) {// меньше 15 минут
                    // вывести (меньше Х минут(минуты)?
                    result = "меньше " + (int) (Math.ceil(delta / 60.0)) + " минут"; // пример: "меньше 15 минут"
                    if (todayMin - minutes < 1) {
                        result = result + "ы"; // пример: "меньше 1 минутЫ"
                    }
                } else {
                    result = hoursAndMinutesFormat.format(newtime);
                }
            } else {
                result = hoursAndMinutesFormat.format(newtime);
            }
        } else {
            result = fullDateFormat.format(newtime);
        }
        return result;
    }

    // Преобразование даты из int в строку для списка контактов
    public static String convertIntDateToStringShort(int Date) {
        // Переводим в миллисекунды и long
        long newtime = ((long) Date) * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(newtime);
        // Получаем год, день, месяц, время, день недели
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        // День недели и месяц превращаем в слова через enum

        now = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        // Разница (в секундах) между текущей датой и датой в сообщении
        int delta = (int)((now.getTimeInMillis() - newtime) / 1000);

        String result = "хрень";
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int todayHour = calendar.get(Calendar.HOUR_OF_DAY);
        int todayMin = today.get(Calendar.MINUTE);

        if (todayYear == year && todayMonth == month && day == todayDay) { // если день не совпадает
            if (todayHour == hour) { // сегодня
                // вывести (часы:минуты)
                if (todayMin - minutes < 15) {// меньше 15 минут
                    // вывести (меньше Х минут(минуты)?
                    result = (int) (Math.ceil(delta / 60.0)) + " мин."; // пример: "меньше 15 минут"
                    if (todayMin - minutes < 1) {
                        result = "только что"; // пример: "меньше 1 минутЫ"
                    }
                } else {
                    result = hoursAndMinutesFormat.format(newtime);
                }
            } else {
                result = hoursAndMinutesFormat.format(newtime);
            }
        } else {
            result = dateFormat.format(newtime);
        }
        return result;
    }

    public static int getDateInt() {
        return (int)(Calendar.getInstance().getTimeInMillis() / 1000);
    }
}
