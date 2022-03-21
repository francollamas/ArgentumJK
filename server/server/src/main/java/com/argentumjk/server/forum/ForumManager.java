/*******************************************************************************
 *     Gorlok AO, an implementation of Argentum Online using Java.
 *     Copyright (C) 2019 Pablo Fernando Lillia «gorlok» 
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.argentumjk.server.forum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.argentumjk.server.Constants;
import com.argentumjk.server.protocol.AddForumMsgResponse;
import com.argentumjk.server.protocol.ShowForumFormResponse;
import com.argentumjk.server.user.User;
import com.argentumjk.server.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author gorlok
 */
public class ForumManager {
	
	

	private static final String FORUM_EXT = ".json";

	private Map<String, Forum> forumCache = new HashMap<>();
	
	private Forum getForum(String forumId) {
		Forum forum = this.forumCache.get(forumId);
		if (forum == null) {
			forum = loadForumFromFile(forumId);
			
			if (forum == null) {
				forum = new Forum(forumId);
				this.forumCache.put(forumId, forum);
			}
		}
		return forum;
	}
	
	private Forum loadForumFromFile(String forumId) {
		String fileName = Constants.FORUM_DIR + File.separator + forumId + FORUM_EXT;

		FileHandle fileHandle = Gdx.files.local(fileName);
		if (!fileHandle.exists()) {
			fileHandle.writeString("", false);
		}

		List<String> lines = fileHandle.reader(100).lines().collect(Collectors.toList());
		String jsonText = Util.join("\n", lines);
		return Forum.fromJson(jsonText);
	}

	private void writeForumToFile(Forum forum) {
		String fileName = Constants.FORUM_DIR + File.separator + forum.getForumId() + FORUM_EXT;

		FileHandle fileHandle = Gdx.files.local(fileName);
		if (!fileHandle.exists()) {
			fileHandle.writeString("", false);
		}

		fileHandle.writeString(forum.toJson(), false);
	}

	public void postOnForum(String forumId, String title, String body, String userName) {
		Forum forum = getForum(forumId);
		
		forum.addPost(title, body, userName);
		
		writeForumToFile(forum);
	}
	
	public void sendForumPosts(String foroId, User user) {
		Forum forum = getForum(foroId);
		
		forum.getPosts().stream()
			.sorted(Comparator.comparing(ForumMessage::getCreateDate, Comparator.nullsLast(Comparator.reverseOrder())))
			.forEach(post -> {
				user.sendPacket(new AddForumMsgResponse(post.getTitle(), post.getBody()));
			});
		
		user.sendPacket(new ShowForumFormResponse());
	}
	
}
