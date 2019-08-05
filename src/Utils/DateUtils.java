package Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    static SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy 'в' HH:mm");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static SimpleDateFormat hoursAndMinutesFormat = new SimpleDateFormat("HH:mm");

    // Преобразование даты из int в строку для Сообщений
    public static String convertIntDateToString(int date) {
        // Переводим в миллисекунды и long
        long dateMillis = ((long) date) * 1000;
        Calendar convertDate = Calendar.getInstance();
        convertDate.setTimeInMillis(dateMillis);
        // Получаем год, день, месяц, время, день недели
        int year = convertDate.get(Calendar.YEAR);
        int day = convertDate.get(Calendar.DAY_OF_MONTH);
        int month = convertDate.get(Calendar.MONTH);
        int hour = convertDate.get(Calendar.HOUR_OF_DAY);
        int minutes = convertDate.get(Calendar.MINUTE);

        // то же для времени сейчас
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);

        String result = "хрень";

        // Разница (в секундах) между текущей датой и датой в сообщении
        int delta = (int)((now.getTimeInMillis() - convertDate.getTimeInMillis()) / 1000 / 60);
        if(nowYear == year && nowMonth == month && day >= nowDay - 1) { // если больше 2 дней
            if (day == nowDay - 1) { // вчера
                // вывести (вчера часы:минуты)
                result = "вчера " + hoursAndMinutesFormat.format(dateMillis);
            } else if (day == nowDay && nowHour == hour) { // сегодня
                // вывести (часы:минуты)
                if (delta < 15) { // меньше 15 минут
                    // вывести (меньше Х минут(минуты)?
                    result = "меньше " + delta + " минут"; // пример: "меньше 15 минут"
                    if (delta <= 1) {
                        result = "только что"; // пример: "только что"
                    }
                } else {
                    result = hoursAndMinutesFormat.format(dateMillis);
                }
            } else {
                result = hoursAndMinutesFormat.format(dateMillis);
            }
        } else {
            result = fullDateFormat.format(dateMillis);
        }
        return result;
    }

    // Преобразование даты из int в строку для списка контактов
    public static String convertIntDateToStringShort(int Date) {
        // Переводим в миллисекунды и long
        long dateMillis = ((long) Date) * 1000;
        Calendar convertDate = Calendar.getInstance();
        convertDate.setTimeInMillis(dateMillis);
        // Получаем год, день, месяц, время, день недели
        int year = convertDate.get(Calendar.YEAR);
        int day = convertDate.get(Calendar.DAY_OF_MONTH);
        int month = convertDate.get(Calendar.MONTH);
        int hour = convertDate.get(Calendar.HOUR_OF_DAY);
        int minutes = convertDate.get(Calendar.MINUTE);

        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);



        String result = "хрень";
        if (nowYear == year && nowMonth == month && day == nowDay) { // если день совпадает
            // сегодня
            int delta = (int)((now.getTimeInMillis() - convertDate.getTimeInMillis()) / 1000 / 60);

            if (delta < 15) { // меньше 15 минут
                // Разница (в секундах) между текущей датой и датой в сообщении

                // вывести (меньше Х минут(минуты)?
                result = delta + " мин."; // пример: "15 мин."
                if (delta < 1) {
                    result = "только что"; // пример: "только что"
                }
            } else {
                // вывести (часы:минуты)
                result = hoursAndMinutesFormat.format(dateMillis);
            }

        } else {
            result = dateFormat.format(dateMillis);
        }
        return result;
    }

    public static int getDateInt() {
        return (int)(Calendar.getInstance().getTimeInMillis() / 1000);
    }
}
