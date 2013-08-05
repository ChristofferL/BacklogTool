/*
 *  The MIT License
 *
 *  Copyright 2012 Sony Mobile Communications AB. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.sonymobile.backlogtool;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;

/**
 * This class represents a note. A Note belongs to a certain Story and user(name), and consists
 * of a message, date and whether is was created by the system or a user.
 */
@Cache(usage = READ_WRITE)
@Entity
@Table(name = "Notes")
public class Note {
    public static final int MESSAGE_LENGTH = 1000;
    public static final String SYSTEM_USER = "SYSTEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(length = 255)
    private String username;

    @Column(length = MESSAGE_LENGTH)
    private String message = "";

    private boolean sysGenerated;

    @JoinColumn(name = "storyId")
    @ManyToOne
    private Story story;

    private Date created;
    private Date modified;

    public Note(String user, String message, Date createdDate,
            boolean sysGenerated, Story s) {
        this.username = user;
        this.message = message;
        this.created = createdDate;
        this.modified = createdDate;
        this.sysGenerated = sysGenerated;
        this.story = s;
    }

    public Note() {

    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id for this note. The server uses this one.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return StringEscapeUtils.escapeHtml(message);
    }

    /**
     * @return Message where <a>-tags have been added around URLs and
     *         newline-chars have been replaced with <br />
     *         .
     */
    @JsonIgnore
    public String getMessageWithLinksAndLineBreaks() {
        return Util.textAsHtmlLinksAndLineBreaks(getMessage());
    }

    /**
     * @param message
     *            The message to set
     */
    public void setMessage(String message) {
        this.message = StringEscapeUtils.unescapeHtml(message);
    }

    /**
     * Set the date of creation
     * 
     * @param date
     *            The date
     */
    public void setCreatedDate(Date date) {
        this.created = date;
    }

    /**
     * Get the creation-date
     * 
     * @return The creation-date
     */
    public Date getCreatedDate() {
        return created;
    }

    /**
     * Set the date for when this Note was last modified
     * 
     * @param date
     *            The date
     */
    public void setModifiedDate(Date date) {
        this.modified = date;
    }

    /**
     * Get the date of last modification
     * 
     * @return The date
     */
    public Date getModifiedDate() {
        return modified;
    }

    /**
     * Set whether this Note was generated by the system or a user
     * 
     * @param systemgenerated
     *            True if generated by system, otherwise false
     */
    public void setSystemGenerated(boolean systemgenerated) {
        this.sysGenerated = systemgenerated;
    }

    /**
     * @return True if the Note was generated by the system, otherwise false
     */
    public boolean isSystemGenerated() {
        return sysGenerated;
    }

    /**
     * Set the Story for this Note
     * 
     * @param story
     *            The Story
     */
    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * @return The Story this Note belongs to
     */
    @JsonIgnore
    public Story getStory() {
        return story;
    }

    /**
     * Set the username of the user that this Note belongs to
     * 
     * @param user
     *            The username
     */
    public void setUser(String user) {
        this.username = user;
    }

    /**
     * Get the username of the user that this Note belongs to
     * 
     * @return The username
     */
    public String getUser() {
        return username;
    }

    /**
     * Get the id of the Story that this Note belongs to
     * 
     * @return The story-id
     */
    public int getStoryId() {
        return story.getId();
    }

    /**
     * Get a Note with created/modified-date set to now, user to SYSTEM_USER,
     * systemGenerated to true, and message and Story as specified by arguments
     * 
     * @param message
     *            The message of the note
     * @param s
     *            The Story of the Note
     * @return A Note with the specified values
     */
    public static Note genSystemNote(String message, Story s) {
        return new Note(Note.SYSTEM_USER, message, new Date(), true, s);
    }

}