package com.kintone.client;

import com.kintone.client.api.space.AddGuestsRequest;
import com.kintone.client.api.space.AddGuestsResponseBody;
import com.kintone.client.api.space.AddSpaceFromTemplateRequest;
import com.kintone.client.api.space.AddSpaceFromTemplateResponseBody;
import com.kintone.client.api.space.AddThreadCommentRequest;
import com.kintone.client.api.space.AddThreadCommentResponseBody;
import com.kintone.client.api.space.AddThreadRequest;
import com.kintone.client.api.space.AddThreadResponseBody;
import com.kintone.client.api.space.DeleteGuestsRequest;
import com.kintone.client.api.space.DeleteGuestsResponseBody;
import com.kintone.client.api.space.DeleteSpaceRequest;
import com.kintone.client.api.space.DeleteSpaceResponseBody;
import com.kintone.client.api.space.GetSpaceMembersRequest;
import com.kintone.client.api.space.GetSpaceMembersResponseBody;
import com.kintone.client.api.space.GetSpaceRequest;
import com.kintone.client.api.space.GetSpaceResponseBody;
import com.kintone.client.api.space.GetSpacesStatisticsRequest;
import com.kintone.client.api.space.GetSpacesStatisticsResponseBody;
import com.kintone.client.api.space.UpdateSpaceBodyRequest;
import com.kintone.client.api.space.UpdateSpaceBodyResponseBody;
import com.kintone.client.api.space.UpdateSpaceGuestsRequest;
import com.kintone.client.api.space.UpdateSpaceGuestsResponseBody;
import com.kintone.client.api.space.UpdateSpaceMembersRequest;
import com.kintone.client.api.space.UpdateSpaceMembersResponseBody;
import com.kintone.client.api.space.UpdateSpaceRequest;
import com.kintone.client.api.space.UpdateSpaceResponseBody;
import com.kintone.client.api.space.UpdateThreadRequest;
import com.kintone.client.api.space.UpdateThreadResponseBody;
import com.kintone.client.model.space.AddedSpaceMember;
import com.kintone.client.model.space.GuestUser;
import com.kintone.client.model.space.SpaceMember;
import com.kintone.client.model.space.ThreadComment;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/** A client that operates space APIs. */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpaceClient {

    private final InternalClient client;
    private final List<ResponseHandler> handlers;

    /**
     * Adds Guest users to kintone. This does not affiliate Guest users with any Guest Spaces, and
     * does not send any invitation emails. To affiliate a Guest user with a Guest Space, follow up
     * this API call with the Update Space Guests API.
     *
     * @param guests the list of Guest user data
     */
    public void addGuests(List<GuestUser> guests) {
        AddGuestsRequest req = new AddGuestsRequest();
        req.setGuests(guests);
        addGuests(req);
    }

    /**
     * Adds Guest users to kintone. This does not affiliate Guest users with any Guest Spaces, and
     * does not send any invitation emails. To affiliate a Guest user with a Guest Space, follow up
     * this API call with the Update Space Guests API.
     *
     * @param request the request parameters. See {@link AddGuestsRequest}
     * @return the response data. See {@link AddGuestsResponseBody}
     */
    public AddGuestsResponseBody addGuests(AddGuestsRequest request) {
        return client.call(KintoneApi.ADD_GUESTS, request, handlers);
    }

    /**
     * Creates a Space from a Space template.
     *
     * @param request the request parameters. See {@link AddSpaceFromTemplateRequest}
     * @return the response data. See {@link AddSpaceFromTemplateResponseBody}
     */
    public AddSpaceFromTemplateResponseBody addSpaceFromTemplate(
            AddSpaceFromTemplateRequest request) {
        return client.call(KintoneApi.ADD_SPACE_FROM_TEMPLATE, request, handlers);
    }

    /**
     * Adds a Thread in a Space.
     *
     * @param spaceId the Space ID
     * @param name the name of the new Thread
     * @return the Thread ID of the created Thread
     */
    public long addThread(long spaceId, String name) {
        AddThreadRequest req = new AddThreadRequest();
        req.setSpace(spaceId);
        req.setName(name);
        return addThread(req).getId();
    }

    /**
     * Adds a Thread in a Space.
     *
     * @param request the request parameters. See {@link AddThreadRequest}
     * @return the response data. See {@link AddThreadResponseBody}
     */
    public AddThreadResponseBody addThread(AddThreadRequest request) {
        return client.call(KintoneApi.ADD_THREAD, request, handlers);
    }

    /**
     * Adds a comment to a Thread of a Space.
     *
     * @param spaceId the Space ID
     * @param threadId the Thread ID
     * @param comment An object including comment details
     * @return the comment ID of the created comment
     */
    public long addThreadComment(long spaceId, long threadId, ThreadComment comment) {
        AddThreadCommentRequest req = new AddThreadCommentRequest();
        req.setSpace(spaceId);
        req.setThread(threadId);
        req.setComment(comment);
        return addThreadComment(req).getId();
    }

    /**
     * Adds a comment to a Thread of a Space.
     *
     * @param request the request parameters. See {@link AddThreadCommentRequest}
     * @return the response data. See {@link AddThreadCommentResponseBody}
     */
    public AddThreadCommentResponseBody addThreadComment(AddThreadCommentRequest request) {
        return client.call(KintoneApi.ADD_THREAD_COMMENT, request, handlers);
    }

    /**
     * Deletes a Guest user from kintone. If you would like to remove a user from a Guest Space
     * without deleting their account, use the Update Guest Members API.
     *
     * @param guests a list of email addresses of Guest users
     */
    public void deleteGuests(List<String> guests) {
        DeleteGuestsRequest req = new DeleteGuestsRequest();
        req.setGuests(guests);
        deleteGuests(req);
    }

    /**
     * Deletes a Guest user from kintone. If you would like to remove a user from a Guest Space
     * without deleting their account, use the Update Guest Members API.
     *
     * @param request the request parameters. See {@link DeleteGuestsRequest}
     * @return the response data. See {@link DeleteGuestsResponseBody}
     */
    public DeleteGuestsResponseBody deleteGuests(DeleteGuestsRequest request) {
        return client.call(KintoneApi.DELETE_GUESTS, request, handlers);
    }

    /**
     * Deletes a Space.
     *
     * @param spaceId the Space ID
     */
    public void deleteSpace(long spaceId) {
        DeleteSpaceRequest req = new DeleteSpaceRequest();
        req.setId(spaceId);
        deleteSpace(req);
    }

    /**
     * Deletes a Space.
     *
     * @param request the request parameters. See {@link DeleteSpaceRequest}
     * @return the response data. See {@link DeleteSpaceResponseBody}
     */
    public DeleteSpaceResponseBody deleteSpace(DeleteSpaceRequest request) {
        return client.call(KintoneApi.DELETE_SPACE, request, handlers);
    }

    /**
     * Gets information of a Space.
     *
     * @param spaceId ths Space ID
     * @return the response data. See {@link GetSpaceResponseBody}
     */
    public GetSpaceResponseBody getSpace(long spaceId) {
        GetSpaceRequest req = new GetSpaceRequest();
        req.setId(spaceId);
        return getSpace(req);
    }

    /**
     * Gets information of a Space.
     *
     * @param request the request parameters. See {@link GetSpaceRequest}
     * @return the response data. See {@link GetSpaceResponseBody}
     */
    public GetSpaceResponseBody getSpace(GetSpaceRequest request) {
        return client.call(KintoneApi.GET_SPACE, request, handlers);
    }

    /**
     * Gets space usage statistics.
     *
     * @return the response data. See {@link GetSpacesStatisticsResponseBody}
     */
    public GetSpacesStatisticsResponseBody getStatistics() {
        return getStatistics(new GetSpacesStatisticsRequest());
    }

    /**
     * Gets space usage statistics.
     *
     * @param request the request parameters. See {@link GetSpacesStatisticsRequest}
     * @return the response data. See {@link GetSpacesStatisticsResponseBody}
     */
    public GetSpacesStatisticsResponseBody getStatistics(GetSpacesStatisticsRequest request) {
        return client.call(KintoneApi.GET_SPACES_STATISTICS, request, handlers);
    }

    /**
     * Gets the list of Space members of a Space.
     *
     * @param spaceId the Space ID
     * @return a list of Space members. Guest users, inactive users and deleted users will not be
     *     included.
     */
    public List<AddedSpaceMember> getSpaceMembers(long spaceId) {
        GetSpaceMembersRequest req = new GetSpaceMembersRequest();
        req.setId(spaceId);
        return getSpaceMembers(req).getMembers();
    }

    /**
     * Gets the list of Space members of a Space.
     *
     * @param request the request parameters. See {@link GetSpaceMembersRequest}
     * @return the response data. See {@link GetSpaceMembersResponseBody}
     */
    public GetSpaceMembersResponseBody getSpaceMembers(GetSpaceMembersRequest request) {
        return client.call(KintoneApi.GET_SPACE_MEMBERS, request, handlers);
    }

    /**
     * Updates the settings of a Space.
     *
     * @param request the request parameters. See {@link UpdateSpaceRequest}
     * @return the response data. See {@link UpdateSpaceResponseBody}
     */
    public UpdateSpaceResponseBody updateSpace(UpdateSpaceRequest request) {
        return client.call(KintoneApi.UPDATE_SPACE, request, handlers);
    }

    /**
     * Updates the body of a Space.
     *
     * @param spaceId the Space ID
     * @param body the contents of the body as an HTML string
     */
    public void updateSpaceBody(long spaceId, String body) {
        UpdateSpaceBodyRequest req = new UpdateSpaceBodyRequest();
        req.setId(spaceId);
        req.setBody(body);
        updateSpaceBody(req);
    }

    /**
     * Updates the body of a Space.
     *
     * @param request the request parameters. See {@link UpdateSpaceBodyRequest}
     * @return the response data. See {@link UpdateSpaceBodyResponseBody}
     */
    public UpdateSpaceBodyResponseBody updateSpaceBody(UpdateSpaceBodyRequest request) {
        return client.call(KintoneApi.UPDATE_SPACE_BODY, request, handlers);
    }

    /**
     * Updates the Guest members of a Space.
     *
     * @param spaceId the Space ID
     * @param guests a list of email addresses of Guest users
     */
    public void updateSpaceGuests(long spaceId, List<String> guests) {
        UpdateSpaceGuestsRequest req = new UpdateSpaceGuestsRequest();
        req.setId(spaceId);
        req.setGuests(guests);
        updateSpaceGuests(req);
    }

    /**
     * Updates the Guest members of a Space.
     *
     * @param request the request parameters. See {@link UpdateSpaceGuestsRequest}
     * @return the response data. See {@link UpdateSpaceGuestsResponseBody}
     */
    public UpdateSpaceGuestsResponseBody updateSpaceGuests(UpdateSpaceGuestsRequest request) {
        return client.call(KintoneApi.UPDATE_SPACE_GUESTS, request, handlers);
    }

    /**
     * Updates the members of a Space.
     *
     * @param spaceId the Space ID
     * @param members a list of members of the Space
     */
    public void updateSpaceMembers(long spaceId, List<SpaceMember> members) {
        UpdateSpaceMembersRequest req = new UpdateSpaceMembersRequest();
        req.setId(spaceId);
        req.setMembers(members);
        updateSpaceMembers(req);
    }

    /**
     * Updates the Members of a Space.
     *
     * @param request the request parameters. See {@link UpdateSpaceMembersRequest}
     * @return the response data. See {@link UpdateSpaceMembersResponseBody}
     */
    public UpdateSpaceMembersResponseBody updateSpaceMembers(UpdateSpaceMembersRequest request) {
        return client.call(KintoneApi.UPDATE_SPACE_MEMBERS, request, handlers);
    }

    /**
     * Updates a Thread of a Space.
     *
     * @param threadId the Thread ID
     * @param name the new name of the Thread
     * @param body the contents of the Thread body
     */
    public void updateThread(long threadId, String name, String body) {
        UpdateThreadRequest req = new UpdateThreadRequest();
        req.setId(threadId);
        req.setName(name);
        req.setBody(body);
        updateThread(req);
    }

    /**
     * Updates a Thread of a Space.
     *
     * @param request the request parameters. See {@link UpdateThreadRequest}
     * @return the response data. See {@link UpdateThreadResponseBody}
     */
    public UpdateThreadResponseBody updateThread(UpdateThreadRequest request) {
        return client.call(KintoneApi.UPDATE_THREAD, request, handlers);
    }
}
