package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.space.AddGuestsRequest;
import com.kintone.client.api.space.AddGuestsResponseBody;
import com.kintone.client.api.space.AddSpaceFromTemplateRequest;
import com.kintone.client.api.space.AddSpaceFromTemplateResponseBody;
import com.kintone.client.api.space.AddThreadCommentRequest;
import com.kintone.client.api.space.AddThreadCommentResponseBody;
import com.kintone.client.api.space.DeleteGuestsRequest;
import com.kintone.client.api.space.DeleteGuestsResponseBody;
import com.kintone.client.api.space.DeleteSpaceRequest;
import com.kintone.client.api.space.DeleteSpaceResponseBody;
import com.kintone.client.api.space.GetSpaceMembersRequest;
import com.kintone.client.api.space.GetSpaceMembersResponseBody;
import com.kintone.client.api.space.GetSpaceRequest;
import com.kintone.client.api.space.GetSpaceResponseBody;
import com.kintone.client.api.space.UpdateSpaceBodyRequest;
import com.kintone.client.api.space.UpdateSpaceBodyResponseBody;
import com.kintone.client.api.space.UpdateSpaceGuestsRequest;
import com.kintone.client.api.space.UpdateSpaceGuestsResponseBody;
import com.kintone.client.api.space.UpdateSpaceMembersRequest;
import com.kintone.client.api.space.UpdateSpaceMembersResponseBody;
import com.kintone.client.api.space.UpdateThreadRequest;
import com.kintone.client.api.space.UpdateThreadResponseBody;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.space.AddedSpaceMember;
import com.kintone.client.model.space.CoverType;
import com.kintone.client.model.space.GuestUser;
import com.kintone.client.model.space.SpaceMember;
import com.kintone.client.model.space.ThreadComment;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class SpaceClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private SpaceClient sut = new SpaceClient(mockClient, Collections.emptyList());

    @Test
    public void addGuests_List() {
        mockClient.setResponseBody(new AddGuestsResponseBody());

        GuestUser guest = new GuestUser().setCode("guest");
        sut.addGuests(Collections.singletonList(guest));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_GUESTS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddGuestsRequest()
                                .setGuests(Collections.singletonList(new GuestUser().setCode("guest"))));
    }

    @Test
    public void addGuests_AddGuestsRequest() {
        AddGuestsRequest req = new AddGuestsRequest();
        AddGuestsResponseBody resp = new AddGuestsResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.addGuests(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_GUESTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addSpaceFromTemplate_AddSpaceFromTemplateRequest() {
        AddSpaceFromTemplateRequest req = new AddSpaceFromTemplateRequest();
        AddSpaceFromTemplateResponseBody resp = new AddSpaceFromTemplateResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addSpaceFromTemplate(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_SPACE_FROM_TEMPLATE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addThreadComment_long_long_ThreadComment() {
        mockClient.setResponseBody(new AddThreadCommentResponseBody(3));

        ThreadComment comment = new ThreadComment().setText("comment");
        assertThat(sut.addThreadComment(1, 2, comment)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_THREAD_COMMENT);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddThreadCommentRequest()
                                .setSpace(1L)
                                .setThread(2L)
                                .setComment(new ThreadComment().setText("comment")));
    }

    @Test
    public void addThreadComment_AddThreadCommentRequest() {
        AddThreadCommentRequest req = new AddThreadCommentRequest();
        AddThreadCommentResponseBody resp = new AddThreadCommentResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addThreadComment(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_THREAD_COMMENT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteGuests_List() {
        mockClient.setResponseBody(new DeleteGuestsResponseBody());

        sut.deleteGuests(Collections.singletonList("guest"));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_GUESTS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new DeleteGuestsRequest().setGuests(Collections.singletonList("guest")));
    }

    @Test
    public void deleteGuests_DeleteGuestsRequest() {
        DeleteGuestsRequest req = new DeleteGuestsRequest();
        DeleteGuestsResponseBody resp = new DeleteGuestsResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteGuests(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_GUESTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteSpace_long() {
        mockClient.setResponseBody(new DeleteSpaceResponseBody());

        sut.deleteSpace(1);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_SPACE);
        assertThat(mockClient.getLastBody()).isEqualTo(new DeleteSpaceRequest().setId(1L));
    }

    @Test
    public void deleteSpace_DeleteSpaceRequest() {
        DeleteSpaceRequest req = new DeleteSpaceRequest();
        DeleteSpaceResponseBody resp = new DeleteSpaceResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteSpace(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_SPACE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getSpace_long() {
        GetSpaceResponseBody resp =
                new GetSpaceResponseBody(
                        3L,
                        "space",
                        10L,
                        false,
                        null,
                        null,
                        CoverType.PRESET,
                        "cover-key",
                        null,
                        "body",
                        null,
                        10,
                        false,
                        false,
                        false);
        mockClient.setResponseBody(resp);

        assertThat(sut.getSpace(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_SPACE);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetSpaceRequest().setId(10L));
    }

    @Test
    public void getSpace_GetSpaceRequest() {
        GetSpaceRequest req = new GetSpaceRequest();
        GetSpaceResponseBody resp =
                new GetSpaceResponseBody(
                        1, "", 1, false, null, null, null, "", "", "", null, 1, false, false, false);
        mockClient.setResponseBody(resp);

        assertThat(sut.getSpace(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_SPACE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getSpaceMembers_long() {
        AddedSpaceMember member =
                new AddedSpaceMember(new Entity(EntityType.USER, "user"), false, false, false);
        mockClient.setResponseBody(new GetSpaceMembersResponseBody(Collections.singletonList(member)));

        assertThat(sut.getSpaceMembers(1))
                .containsExactly(
                        new AddedSpaceMember(new Entity(EntityType.USER, "user"), false, false, false));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_SPACE_MEMBERS);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetSpaceMembersRequest().setId(1L));
    }

    @Test
    public void getSpaceMembers_GetSpaceMembersRequest() {
        GetSpaceMembersRequest req = new GetSpaceMembersRequest();
        GetSpaceMembersResponseBody resp = new GetSpaceMembersResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.getSpaceMembers(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_SPACE_MEMBERS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateSpaceBody_long_String() {
        mockClient.setResponseBody(new UpdateSpaceBodyResponseBody());

        sut.updateSpaceBody(1, "body");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_BODY);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new UpdateSpaceBodyRequest().setId(1L).setBody("body"));
    }

    @Test
    public void updateSpaceBody_UpdateSpaceBodyRequest() {
        UpdateSpaceBodyRequest req = new UpdateSpaceBodyRequest();
        UpdateSpaceBodyResponseBody resp = new UpdateSpaceBodyResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.updateSpaceBody(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_BODY);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateSpaceGuests_long_List() {
        mockClient.setResponseBody(new UpdateSpaceGuestsResponseBody());

        sut.updateSpaceGuests(1, Collections.singletonList("guest"));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_GUESTS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateSpaceGuestsRequest().setId(1L).setGuests(Collections.singletonList("guest")));
    }

    @Test
    public void updateSpaceGuests_UpdateSpaceGuestsRequest() {
        UpdateSpaceGuestsRequest req = new UpdateSpaceGuestsRequest();
        UpdateSpaceGuestsResponseBody resp = new UpdateSpaceGuestsResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.updateSpaceGuests(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_GUESTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateSpaceMembers_long_List() {
        mockClient.setResponseBody(new UpdateSpaceMembersResponseBody());

        SpaceMember member = new SpaceMember().setEntity(new Entity(EntityType.USER, "user"));
        sut.updateSpaceMembers(1L, Collections.singletonList(member));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_MEMBERS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateSpaceMembersRequest()
                                .setId(1L)
                                .setMembers(
                                        Collections.singletonList(
                                                new SpaceMember().setEntity(new Entity(EntityType.USER, "user")))));
    }

    @Test
    public void updateSpaceMembers_UpdateSpaceMembersRequest() {
        UpdateSpaceMembersRequest req = new UpdateSpaceMembersRequest();
        UpdateSpaceMembersResponseBody resp = new UpdateSpaceMembersResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.updateSpaceMembers(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_SPACE_MEMBERS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateThread_long_String_String() {
        mockClient.setResponseBody(new UpdateThreadResponseBody());

        sut.updateThread(1, "name", "body");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_THREAD);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new UpdateThreadRequest().setId(1L).setName("name").setBody("body"));
    }

    @Test
    public void updateThread_UpdateThreadRequest() {
        UpdateThreadRequest req = new UpdateThreadRequest();
        UpdateThreadResponseBody resp = new UpdateThreadResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.updateThread(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_THREAD);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }
}
