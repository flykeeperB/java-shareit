package ru.practicum.shareit.TestDataGenerator;

import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Long lastCommentId = 0L;
    public Long lastItemRequestId = 0L;

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

    public ItemExtraDto generateItemExtraDto() {
        ItemExtraDto result = new ItemExtraDto();
        lastItemId++;
        result.setId(lastItemId);
        String itemName = getRandomStringValue(itemNames);
        result.setName(itemName + lastItemId);
        result.setDescription("Это отличный " + itemName);
        result.setOwner(generateUserDto());
        result.setAvailable(true);
        result.setLastBooking(generateBookingDto());
        result.setNextBooking(generateBookingDto());
        return result;
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
        result.setItemId(result.getItem().getId());
        result.setBooker(generateUserDto());
        result.setBookerId(result.getBooker().getId());
        result.setStatus(BookingStatus.WAITING);

        return result;
    }

    public CommentDto generateOnlyTextCommentDto() {
        CommentDto result = new CommentDto();

        result.setText("отличный комментарий " + lastCommentId);

        return result;
    }

    public CommentDto generateDetailedCommentDto() {
        CommentDto result = generateOnlyTextCommentDto();

        lastCommentId++;
        result.setId(lastCommentId);
        result.setAuthorName(makeName(Sex.RANDOM));
        int createTime = (int) (10000 * Math.random());
        result.setCreated(LocalDateTime.now().minusMinutes(createTime));

        return result;
    }

    public Comment generateComment() {
        Comment result = new Comment();

        lastCommentId++;
        result.setId(lastCommentId);
        result.setAuthor(generateUser());
        result.setItem(generateItem());
        int createTime = (int) (10000 * Math.random());
        result.setCreated(LocalDateTime.now().minusMinutes(createTime));
        result.setText("отличный комментарий " + lastCommentId);

        return result;
    }

    public ItemRequest generateItemRequest() {
        ItemRequest result = new ItemRequest();

        lastItemRequestId++;
        result.setId(lastItemRequestId);
        result.setDescription("хотелось взять напрокат " + getRandomStringValue(itemNames));
        result.setRequestor(generateUser());
        int createTime = (int) (10000 * Math.random());
        result.setCreated(LocalDateTime.now().minusMinutes(createTime));

        return result;
    }

    public ItemRequestDto generateItemRequestWithOnlyTextDto() {
        ItemRequestDto result = new ItemRequestDto();
        result.setDescription("хотелось взять напрокат " + getRandomStringValue(itemNames));
        return result;
    }

    public ItemRequestDto generateItemRequestDto() {
        ItemRequestDto result = new ItemRequestDto();
        lastItemRequestId++;
        result.setId(lastItemRequestId);
        result.setRequestor(generateUserDto());
        result.setDescription("хотелось взять напрокат " + getRandomStringValue(itemNames));
        return result;
    }

    public ExtraItemRequestDto generateExtraItemRequestDto() {
        ExtraItemRequestDto result = new ExtraItemRequestDto();

        lastItemRequestId++;
        result.setId(lastItemRequestId);

        result.setItems(new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            result.getItems().add(generateItemDto());
        }
        result.setRequestor(generateUserDto());
        result.setDescription("хотелось взять напрокат " + getRandomStringValue(itemNames));

        return result;
    }

    public CreateItemRequestDto generateCreateItemRequestDto() {
        CreateItemRequestDto result = new CreateItemRequestDto();
        result.setDescription("хотелось взять напрокат " + getRandomStringValue(itemNames));
        return result;
    }

    private String getRandomStringValue(List<String> source) {
        return source.get((int) (Math.random() * source.size()));
    }

}
