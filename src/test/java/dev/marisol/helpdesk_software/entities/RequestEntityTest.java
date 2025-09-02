package dev.marisol.helpdesk_software.entities;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class RequestEntityTest {
    
    @Test 
    public void shouldInitializeWithSetters() {

        RequestEntity r = new RequestEntity();

        r.setId(1L);
        r.setApplicantName("María");
        r.setDescription("No enciende el pc");

        assertThat(r.getId(), is(equalTo(1L)));
        assertThat(r.getApplicantName(), is(equalTo("María")));
        assertThat(r.getDescription(), is(equalTo("No enciende el pc")));
    }
}