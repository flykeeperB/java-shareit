package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractModel;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends AbstractModel {

    @Column(length = 255)
    private String name;

    private String description;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Transient
    private Booking lastBooking;

    @Transient
    private Booking nextBooking;

    @Transient
    private List<Comment> comments;

    /*
    TODO itemRequest
    */
    /*
    TODO transactions
    */
    //private ItemRequest itemRequest;

    /*public Item(Item source) {
        super(source);
        this.description = source.getDescription();
        this.name = source.getName();
        //this.itemRequest = source.getItemRequest();
        this.owner = source.getOwner();
        this.available = source.getAvailable();
    }*/
}
