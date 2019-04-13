package org.pursuit.school_trip_assistant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pursuit.school_trip_assistant.model.Student;
import org.pursuit.school_trip_assistant.viewmodel.StudentsViewModel;
import org.pursuit.school_trip_assistant.viewmodel.TripViewModel;

import static org.junit.Assert.*;

public final class TripViewModelTest {
  private TripViewModel viewModel;

  @Before
  public void setup() {
    viewModel = new TripViewModel();
    viewModel.setDate(2019, 0, 1);
  }

  @Test
  public void testGetPresentableDate() {
    assertEquals("Tue, Jan 1, 2019", viewModel.getDatePresentable());
  }

  @After
  public void tearDown() {
    viewModel = null;
  }
}