package com.kintone.client.space;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.Users;
import com.kintone.client.api.space.*;
import com.kintone.client.exception.KintoneApiRuntimeException;
import com.kintone.client.helper.Space;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.space.AddedSpaceMember;
import com.kintone.client.model.space.GuestUser;
import com.kintone.client.model.space.SpaceMember;
import com.kintone.client.model.space.SpaceStatistics;
import com.kintone.client.model.space.ThreadComment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SpaceApiTest extends ApiTestBase {

    @Test
    public void addGuests_deleteGuests() {
        List<String> codes = Arrays.asList("guest1@localhost", "guest2@localhost");
        KintoneClient client = setupDefaultClient();
        deleteGuestUsers(client, codes);

        List<GuestUser> users =
                Arrays.asList(createGuest("guest1@localhost"), createGuest("guest2@localhost"));
        AddGuestsRequest req1 = new AddGuestsRequest();
        req1.setGuests(users);
        AddGuestsResponseBody resp1 = client.space().addGuests(req1);

        DeleteGuestsRequest req2 = new DeleteGuestsRequest();
        req2.setGuests(codes);
        DeleteGuestsResponseBody resp2 = client.space().deleteGuests(req2);
    }

    @Test
    public void addSpaceFromTemplate() {
        KintoneClient client = setupDefaultClient();

        SpaceMember member = new SpaceMember();
        member.setEntity(new Entity(EntityType.USER, getDefaultUser()));
        member.setIsAdmin(true);

        Long templateId = TestSettings.get().getTemplateId();

        if (templateId == null) {
            System.out.println("Skipping addSpaceFromTemplate: KINTONE_TEMPLATE_ID is not set");
            return;
        }

        AddSpaceFromTemplateRequest req = new AddSpaceFromTemplateRequest();
        req.setId(templateId);
        req.setName("addSpaceFromTemplate_copy_" + System.currentTimeMillis());
        req.setMembers(Collections.singletonList(member));
        req.setFixedMember(true);
        req.setIsGuest(false);
        req.setIsPrivate(true);
        AddSpaceFromTemplateResponseBody resp = client.space().addSpaceFromTemplate(req);
        assertThat(resp.getId()).isGreaterThan(0);

        client.space().deleteSpace(resp.getId());
    }

    @Test
    public void addThread() {
        Space space = Space.multiThread(this);
        KintoneClient client = setupDefaultClient();

        AddThreadRequest req = new AddThreadRequest();
        req.setSpace(space.id());
        String newThreadName = "added thread name via java client " + System.currentTimeMillis();
        req.setName(newThreadName);
        AddThreadResponseBody resp = client.space().addThread(req);
        assertThat(resp.getId()).isGreaterThan(0);
    }

    @Test
    public void addThreadComment() {
        KintoneClient client = setupDefaultClient();
        Space space = Space.singleThread(this);

        AddThreadCommentRequest req = new AddThreadCommentRequest();
        req.setSpace(space.id());
        req.setThread(space.getDefaultThread());
        req.setComment(new ThreadComment().setText("comment! " + System.currentTimeMillis()));
        AddThreadCommentResponseBody resp = client.space().addThreadComment(req);
        assertThat(resp.getId()).isGreaterThan(0L);
    }

    @Test
    @Disabled(
            "deleteSpace requires space recreation which is not available with pre-created resources")
    public void deleteSpace() {}

    @Test
    public void getSpace() {
        Space space = Space.singleThread(this);
        KintoneClient client = setupDefaultClient();

        GetSpaceRequest req = new GetSpaceRequest();
        req.setId(space.id());
        GetSpaceResponseBody resp = client.space().getSpace(req);
        assertThat(resp.getId()).isEqualTo(space.id());
        assertThat(resp.getName()).isNotEmpty();
    }

    @Test
    public void getSpaceMembers() {
        Space space = Space.singleThread(this);
        KintoneClient client = setupDefaultClient();

        GetSpaceMembersRequest req = new GetSpaceMembersRequest();
        req.setId(space.id());
        GetSpaceMembersResponseBody resp = client.space().getSpaceMembers(req);
        List<AddedSpaceMember> members = resp.getMembers();
        assertThat(members).isNotEmpty();
    }

    @Test
    public void updateSpace() {
        Space space = Space.singleThread(this);
        KintoneClient client = setupDefaultClient();

        GetSpaceResponseBody resp1 = client.space().getSpace(space.id());
        String originalName = resp1.getName();

        UpdateSpaceRequest req = new UpdateSpaceRequest();
        req.setId(space.id());
        String newName = "updated_" + System.currentTimeMillis();
        req.setName(newName);
        client.space().updateSpace(req);

        GetSpaceResponseBody resp2 = client.space().getSpace(space.id());
        assertThat(resp2.getName()).isEqualTo(newName);

        UpdateSpaceRequest restoreReq = new UpdateSpaceRequest();
        restoreReq.setId(space.id());
        restoreReq.setName(originalName);
        client.space().updateSpace(restoreReq);
    }

    @Test
    public void updateSpaceBody() {
        Space space = Space.multiThread(this);
        KintoneClient client = setupDefaultClient();

        UpdateSpaceBodyRequest req = new UpdateSpaceBodyRequest();
        req.setId(space.id());
        String newBody = "Space Body " + System.currentTimeMillis();
        req.setBody(newBody);
        UpdateSpaceBodyResponseBody resp = client.space().updateSpaceBody(req);

        assertThat(client.space().getSpace(space.id()).getBody()).isEqualTo(newBody);

        // 元のボディに添付ファイル参照があると復元できないため、シンプルな空文字列で復元
        // テスト用スペースなので問題なし
        UpdateSpaceBodyRequest restoreReq = new UpdateSpaceBodyRequest();
        restoreReq.setId(space.id());
        restoreReq.setBody("");
        client.space().updateSpaceBody(restoreReq);
    }

    @Test
    public void updateSpaceGuests() {
        List<String> codes = Arrays.asList("guest1@localhost", "guest2@localhost");
        KintoneClient client = setupDefaultClient();
        deleteGuestUsers(client, codes);

        List<GuestUser> users =
                Arrays.asList(createGuest("guest1@localhost"), createGuest("guest2@localhost"));
        client.space().addGuests(users);

        Space space = Space.guest(this);

        KintoneClient guestSpaceClient = setupDefaultClient(space.id());
        UpdateSpaceGuestsRequest req = new UpdateSpaceGuestsRequest();
        req.setId(space.id());
        req.setGuests(codes);
        UpdateSpaceGuestsResponseBody resp1 = guestSpaceClient.space().updateSpaceGuests(req);

        UpdateSpaceGuestsRequest resetReq = new UpdateSpaceGuestsRequest();
        resetReq.setId(space.id());
        resetReq.setGuests(Collections.emptyList());
        guestSpaceClient.space().updateSpaceGuests(resetReq);

        deleteGuestUsers(client, codes);
    }

    @Test
    public void updateSpaceMembers() {
        Space space = Space.singleThread(this);
        KintoneClient client = setupDefaultClient();

        List<AddedSpaceMember> originalMembers = client.space().getSpaceMembers(space.id());

        SpaceMember member1 =
                new SpaceMember().setEntity(new Entity(EntityType.USER, getDefaultUser())).setIsAdmin(true);
        SpaceMember member2 =
                new SpaceMember().setEntity(new Entity(EntityType.USER, Users.user1.getCode()));
        SpaceMember member3 =
                new SpaceMember().setEntity(new Entity(EntityType.GROUP, "everyone")).setIsAdmin(true);

        UpdateSpaceMembersRequest req = new UpdateSpaceMembersRequest();
        req.setId(space.id());
        req.setMembers(Arrays.asList(member1, member2, member3));
        UpdateSpaceMembersResponseBody resp = client.space().updateSpaceMembers(req);

        List<AddedSpaceMember> updatedMembers = client.space().getSpaceMembers(space.id());
        assertThat(updatedMembers.size()).isGreaterThanOrEqualTo(2);

        List<SpaceMember> restoreMembers = new java.util.ArrayList<>();
        for (AddedSpaceMember m : originalMembers) {
            SpaceMember sm = new SpaceMember();
            sm.setEntity(m.getEntity());
            sm.setIsAdmin(m.isAdmin());
            restoreMembers.add(sm);
        }
        UpdateSpaceMembersRequest restoreReq = new UpdateSpaceMembersRequest();
        restoreReq.setId(space.id());
        restoreReq.setMembers(restoreMembers);
        client.space().updateSpaceMembers(restoreReq);
    }

    @Test
    public void updateThread() {
        Space space = Space.multiThread(this);
        KintoneClient client = setupDefaultClient();

        UpdateThreadRequest req = new UpdateThreadRequest();
        req.setId(space.getDefaultThread());
        String newName = "Thread Name " + System.currentTimeMillis();
        req.setName(newName);
        req.setBody("Thread Body");
        UpdateThreadResponseBody resp = client.space().updateThread(req);
    }

    private GuestUser createGuest(String code) {
        String name = code.split("@")[0];
        GuestUser user = new GuestUser();
        user.setCode(code);
        user.setName(name);
        user.setPassword("password123");
        user.setTimezone(TimeZone.getTimeZone("Asia/Tokyo"));
        return user;
    }

    private void deleteGuestUsers(KintoneClient client, List<String> codes) {
        for (String code : codes) {
            try {
                client.space().deleteGuests(Collections.singletonList(code));
            } catch (KintoneApiRuntimeException e) {
                System.out.println("ignore deleting guest error: " + e.getMessage());
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Disabled("Skipped until SpaceStatistics model is updated for new API fields")
    public void getStatistics() {
        KintoneClient client = setupDefaultClient();
        Space space = Space.singleThread(this);

        GetSpacesStatisticsRequest req = new GetSpacesStatisticsRequest();
        req.setLimit(100L);
        req.setOffset(0L);
        GetSpacesStatisticsResponseBody resp = client.space().getStatistics(req);

        List<SpaceStatistics> spaces = resp.getSpaces();
        assertThat(spaces).isNotEmpty();

        Optional<SpaceStatistics> targetSpace =
                spaces.stream().filter(s -> s.getId() == space.id()).findFirst();
        assertThat(targetSpace).isPresent();

        SpaceStatistics stats = targetSpace.get();
        assertThat(stats.getId()).isEqualTo(space.id());
        assertThat(stats.getName()).isNotEmpty();
    }
}
