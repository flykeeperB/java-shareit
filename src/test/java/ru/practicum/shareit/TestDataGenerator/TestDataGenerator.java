package ru.practicum.shareit.TestDataGenerator;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataGenerator {

    public Map<Sex, List<String>> firstNames;
    public Map<Sex, List<String>> secondNames;
    public Map<Sex, List<String>> lastNames;
    public List<String> itemNames;
    public Long lastUserId = 0L;
    public Long lastItemId = 0L;
    public Long lastBookingId = 0L;
    public Long lastRequestId = 0L;

    public TestDataGenerator() {
        firstNames = new HashMap<>();
        secondNames = new HashMap<>();
        lastNames = new HashMap<>();

        firstNames.put(Sex.MALE, List.of("Александр",
                "Алексей", "Арсений", "Борис",
                "Валерий", "Виктор", "Владимир",
                "Григорий", "Дмитрий", "Егор",
                "Игорь", "Константин", "Николай",
                "Олег", "Павел", "Роман",
                "Стапан", "Тимур", "Тимофей",
                "Федор", "Юрий"
        ));

        firstNames.put(Sex.FEMALE, List.of("Александра",
                "Анастасия", "Алевтина",
                "Валерия", "Виктория",
                "Дарья", "Евгения", "Зинаида",
                "Ира", "Кристина", "Наталья",
                "Ольга", "Полина", "Регина",
                "Светлана", "Татьяна", "Ульяна",
                "Фаина", "Юлия"
        ));

        secondNames.put(Sex.MALE, List.of("Александрович",
                "Алексеевич", "Арсеньевич", "Борисович",
                "Валерьевич", "Викторович", "Владимирович",
                "Григорьевич", "Дмитриевич", "Егорович",
                "Игоревич", "Константинович", "Николаевич",
                "Олегович", "Павлович", "Романович",
                "Степанович", "Тимурович", "Тимофеевич",
                "Федорович", "Юрьевич"
        ));

        secondNames.put(Sex.FEMALE, List.of("Александровна",
                "Алексеевна", "Арсеньевна", "Борисовна",
                "Валерьевна", "Викторовна", "Владимировна",
                "Григорьевна", "Дмитриевна", "Егоровна",
                "Игоревна", "Константиновна", "Николаевна",
                "Олеговна", "Павловна", "Романовна",
                "Степановна", "Тимуровна", "Тимофеевна",
                "Федоровна", "Юрьевна"
        ));

        lastNames.put(Sex.MALE, List.of("Александров",
                "Алексеев", "Арсеньев", "Борисов",
                "Валерьев", "Викторов", "Владимиров",
                "Григорьев", "Дмитров", "Егоров",
                "Игорев", "Константинов", "Николаев",
                "Олегов", "Павлов", "Романов",
                "Степанов", "Тимуров", "Тимофеев",
                "Федоров", "Юрьев"
        ));

        lastNames.put(Sex.FEMALE, List.of("Александрова",
                "Алексеева", "Арсеньева", "Борисова",
                "Валерьева", "Викторова", "Владимирова",
                "Григорьева", "Дмитриева", "Егорова",
                "Игорева", "Константинова", "Николаева",
                "Олегова", "Павлова", "Романова",
                "Степанова", "Тимурова", "Тимофеева",
                "Федорова", "Юрьева"
        ));

        itemNames = List.of("телевизор",
                "автомобиль",
                "самокат",
                "самолет",
                "лодка",
                "дрель",
                "отвертка",
                "тостер",
                "пуговица",
                "костюм деда мороза",
                "костюм русалочки",
                "утюг",
                "велосипед",
                "компьютер",
                "паяльник",
                "линейка");
    }

    private String getRandomStringValue(List<String> source) {
        return source.get((int) (Math.random() * source.size()));
    }

    public String makeName(Sex sex) {
        if (sex.equals(Sex.RANDOM)) {
            sex = Math.random() > 0.5 ? Sex.MALE : Sex.FEMALE;
        }
        return getRandomStringValue(firstNames.get(sex)) + " " +
                getRandomStringValue(secondNames.get(sex)) + " " +
                getRandomStringValue(lastNames.get(sex));
    }

    public User generateUser() {
        return new User(++lastUserId,
                makeName(Sex.RANDOM),
                "usermail" + lastUserId + "@ya.ru");
    }

    public UserDto generateUserDto() {
        return new UserDto(++lastUserId,
                makeName(Sex.RANDOM),
                "usermail" + lastUserId + "@ya.ru");
    }

    public Item generateItem() {
        String itemName = getRandomStringValue(itemNames);
        return new Item(++lastItemId,
                itemName,
                "Это отличный " + itemName,
                true,
                generateUser(),
                null,
                null,
                null,
                null
        );
    }

    public ItemDto generateItemDto() {
        String itemName = getRandomStringValue(itemNames);
        return new ItemDto(++lastItemId,
                itemName,
                "Это отличный " + itemName,
                true,
                generateUserDto(),
                null,
                null
        );
    }

    public Booking generateBooking() {
        LocalDateTime bookingMoment = LocalDateTime.now().plusHours(10);
        return new Booking(++lastBookingId,
                bookingMoment,
                bookingMoment.plusDays(2),
                generateItem(),
                generateUser(),
                BookingStatus.WAITING
        );
    }

    public BookingExtraDto generateBookingDto() {
        LocalDateTime bookingMoment = LocalDateTime.now().plusHours(10);
        BookingExtraDto result = new BookingExtraDto();

        result.setId(++lastBookingId);
        result.setStart(bookingMoment);
        result.setEnd(bookingMoment);
        result.setItem(generateItemDto());
        result.setBooker(generateUserDto());
        result.setStatus(BookingStatus.WAITING);

        return result;
    }

}