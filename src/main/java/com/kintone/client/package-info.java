/**
 * Provides the core classes of the API client for Kintone APIs.
 *
 * <p>{@link com.kintone.client.KintoneClient} is the API client, and {@link
 * com.kintone.client.KintoneClientBuilder} is the builder class for a {@code KintoneClient}. To
 * setup the client, see the documents of these classes.
 *
 * <p>{@code KintoneClient} delegates actual API calls to following sub-components, except for the
 * bulk requests:
 *
 * <ul>
 *   <li>{@link com.kintone.client.RecordClient} for APIs related to record operations
 *   <li>{@link com.kintone.client.AppClient} for APIs related to App settings
 *   <li>{@link com.kintone.client.FileClient} for uploading and downloading files APIs
 *   <li>{@link com.kintone.client.SpaceClient} for APIs related to Space settings
 *   <li>{@link com.kintone.client.SchemaClient} for the API Schema APIs
 * </ul>
 *
 * See these classes to find the documents of specific APIs.
 */
package com.kintone.client;
