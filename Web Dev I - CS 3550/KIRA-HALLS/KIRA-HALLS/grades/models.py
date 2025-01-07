from django.db import models
from django.contrib.auth.models import User, Group
from django.core.exceptions import ValidationError
from django.core.exceptions import PermissionDenied


def is_within_range(value):
    if value >= 0 and value <= 100:
         return True
    else: 
        raise ValidationError

class Assignment(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField(null=True, blank=True)
    deadline = models.DateTimeField()
    weight = models.IntegerField(validators=[is_within_range])
    points = models.IntegerField(validators=[is_within_range])
          

class Submission(models.Model):
    # If assignment is deleted, we don't need submissions anymore, so CASCADE
    assignment = models.ForeignKey(Assignment, on_delete=models.CASCADE, null=False, blank=False)

    # If author is deleted, then the submission won't need to be graded, so CASCADE
    author = models.ForeignKey(User, on_delete=models.CASCADE, null=False, blank=False)

    # If the grader is deleted, the assignment will be graded by someone else, not deleted so SET_NULL
    grader = models.ForeignKey(User, on_delete=models.SET_NULL, related_name='graded_set', null=True, blank=True)
    file = models.FileField(unique=True, null=False, blank=False)
    score = models.FloatField(validators=[is_within_range], null=True, blank=True)

    def is_within_range(value):
        if value >= 0 and value <= 100:
            return True
        else: 
            raise ValidationError
        
    def change_grade(self, user, new_grade):
        anonymous_user = not user.is_authenticated
        # If the user is logged in, then check for their status
        if not anonymous_user:
            is_ta = user.groups.filter(name="Teaching Assistants").exists()
            is_administrator = user.is_superuser
        if not (is_ta or is_administrator):
            raise PermissionDenied 
        else: 
            self.score = new_grade
        
    def view_submission(self, user):
        # If user is the submission author, let them view
        if user == self.author:
            return self.file
        # If user is a ta, let them view
        elif user.groups.filter(name="Teaching Assistants").exists():
            return self.file
        # If user is an admin, let them view
        elif user.is_superuser:
            return self.file
        # If none of the above, don't let them view
        else: 
            raise PermissionDenied
       

