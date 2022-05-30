package org.isoron.uhabits.core

import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.isoron.uhabits.core.models.Habit
import org.isoron.uhabits.core.models.PaletteColor
import org.isoron.uhabits.core.ui.callbacks.OnColorPickedCallback
import org.isoron.uhabits.core.ui.callbacks.OnConfirmedCallback
import org.isoron.uhabits.core.ui.screens.habits.list.ListHabitsSelectionMenuBehavior
import org.junit.Test

class UserProfileTest : BaseUnitTest() {
    private val screen: ListHabitsSelectionMenuBehavior.Screen = mock()

    private val adapter: ListHabitsSelectionMenuBehavior.Adapter = mock()
    private lateinit var behavior: ListHabitsSelectionMenuBehavior
    private lateinit var habit1: Habit
    private lateinit var habit2: Habit
    private lateinit var habit3: Habit

    private val colorPickerCallback: KArgumentCaptor<OnColorPickedCallback> = argumentCaptor()

    private val deleteCallback: KArgumentCaptor<OnConfirmedCallback> = argumentCaptor()

    @Test
    @Throws(Exception::class)
    fun canEdit() {
        whenever(adapter.getSelected()).thenReturn(listOf(habit1))
        Assert.assertTrue(behavior.canEdit())
        whenever(adapter.getSelected()).thenReturn(listOf(habit1, habit2))
        Assert.assertFalse(behavior.canEdit())
    }

    @Test
    @Throws(Exception::class)
    fun canUnarchive() {
        whenever(adapter.getSelected()).thenReturn(listOf(habit1, habit2))
        Assert.assertFalse(behavior.canUnarchive())
        whenever(adapter.getSelected()).thenReturn(listOf(habit1))
        Assert.assertTrue(behavior.canUnarchive())
    }

    @Test
    @Throws(Exception::class)
    fun onArchiveHabits() {
        Assert.assertFalse(habit2.isArchived)
        whenever(adapter.getSelected()).thenReturn(listOf(habit2))
        behavior.onArchiveHabits()
        Assert.assertTrue(habit2.isArchived)
    }

    @Test
    @Throws(Exception::class)
    fun onChangeColor() {
        MatcherAssert.assertThat(habit1.color, Matchers.equalTo(PaletteColor(8)))
        MatcherAssert.assertThat(habit2.color, Matchers.equalTo(PaletteColor(8)))
        whenever(adapter.getSelected()).thenReturn(listOf(habit1, habit2))
        behavior.onChangeColor()
        verify(screen)
            .showColorPicker(eq(PaletteColor(8)), colorPickerCallback.capture())
        colorPickerCallback.lastValue.onColorPicked(PaletteColor(30))
        MatcherAssert.assertThat(habit1.color, Matchers.equalTo(PaletteColor(30)))
    }

    @Test
    @Throws(Exception::class)
    fun onDeleteHabits() {
        val id = habit1.id!!
        habitList.getById(id)!!
        whenever(adapter.getSelected()).thenReturn(listOf(habit1))
        behavior.onDeleteHabits()
        verify(screen).showDeleteConfirmationScreen(deleteCallback.capture(), eq(1))
        deleteCallback.lastValue.onConfirmed()
        Assert.assertNull(habitList.getById(id))
    }

    @Test
    @Throws(Exception::class)
    fun onEditHabits() {
        val selected: List<Habit> = listOf(habit1, habit2)
        whenever(adapter.getSelected()).thenReturn(selected)
        behavior.onEditHabits()
        verify(screen).showEditHabitsScreen(selected)
    }

    @Test
    @Throws(Exception::class)
    fun onUnarchiveHabits() {
        Assert.assertTrue(habit1.isArchived)
        whenever(adapter.getSelected()).thenReturn(listOf(habit1))
        behavior.onUnarchiveHabits()
        Assert.assertFalse(habit1.isArchived)
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        habit1 = fixtures.createShortHabit()
        habit1.isArchived = true
        habit2 = fixtures.createShortHabit()
        habit3 = fixtures.createShortHabit()
        habitList.add(habit1)
        habitList.add(habit2)
        habitList.add(habit3)
        behavior = ListHabitsSelectionMenuBehavior(
            habitList,
            screen,
            adapter,
            commandRunner
        )
    }

}
