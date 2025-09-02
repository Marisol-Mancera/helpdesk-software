package dev.marisol.helpdesk_software.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TopicEntityTest {

    @Test
    public void shouldInitializeTopicWithSetters() {

        TopicEntity t = new TopicEntity();

        t.setId(1L);
        t.setName("Hardware");
        t.setActive(true);

        assertThat(t.getId(), is(equalTo(1L)));
        assertThat(t.getName(), is(equalTo("Hardware")));
        assertThat(t.isActive(), is(true));
    }

    @Test
    public void shouldThrowExceptionWhenNameIsNull() {

        TopicEntity t = new TopicEntity();

        assertThrows(NullPointerException.class, () -> t.setName(null));
    }

    @Test
    public void shouldToggleActiveFlag(){

        TopicEntity t = new TopicEntity();

        t.setActive(true);

        assertThat(t.isActive(), is(true));

         t.setActive(false);
         
        assertThat(t.isActive(), is(false));
    }

}