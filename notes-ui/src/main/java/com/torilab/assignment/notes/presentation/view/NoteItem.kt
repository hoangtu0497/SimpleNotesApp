package com.torilab.assignment.notes.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.torilab.assignment.foundations.util.DateUtil
import com.torilab.assignment.note.domain.model.Note

@Composable
internal fun NoteItem(
    data: Note,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 0.dp)
                .background(MaterialTheme.colorScheme.surface),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = data.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = data.note,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 1.dp),
                )
                Text(
                    text = DateUtil.convertDateString(DATE_FORMAT_STRING, data.createdAt.toString()),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}

private const val DATE_FORMAT_STRING = "EEEE, MMMM d h:mm a"
