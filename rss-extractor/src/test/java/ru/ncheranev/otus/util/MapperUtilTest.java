package ru.ncheranev.otus.util;

import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MapperUtilTest {

    @Test
    @DisplayName("должен маппить значения полей, если поля заполнены")
    void toDto_shouldMappingWithNotEmptyFields() {
        // given
        SyndEntry syndEntry = new SyndEntryImpl();
        syndEntry.setDescription(new SyndContentImpl() {{ setValue("description"); }});
        syndEntry.setPublishedDate(new Date());
        syndEntry.setSource(new SyndFeedImpl());

        // when
        var actual = MapperUtil.toDto(syndEntry);

        // then
        assertThat(actual).isNotNull();
    }
}