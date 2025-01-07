import datetime
from django.forms import ValidationError
from django.http import Http404, HttpResponse, HttpResponseBadRequest
from django.shortcuts import get_object_or_404, redirect, render
from . import models
from grades.models import Submission, User
from django.contrib.auth import authenticate, login, logout
from django.utils import timezone
from django.db.models import Q, Count
from django.contrib.auth.decorators import login_required
from django.utils.http import url_has_allowed_host_and_scheme
from django.core.exceptions import PermissionDenied

@login_required
# Create your views here.
def index(request):
    assignments = models.Assignment.objects.all()
    return render(request, "index.html", {
        "assignments": assignments,
    })

@login_required
def assignment(request, assignment_id): 
    # Get currently logged in user and check for student, ta, and administrator values
    current_user = request.user
    anonymous_user = not current_user.is_authenticated
    # If the user is logged in, then check for their status
    if not anonymous_user:
        is_student = current_user.groups.filter(name="Students").exists()
        is_ta = current_user.groups.filter(name="Teaching Assistants").exists()
        is_administrator = current_user.is_superuser
    
    # Get assignment object and its submissions   
    assignment = get_object_or_404(models.Assignment, id=assignment_id)
    submissions = assignment.submission_set.count()

    # If user is a ta, then get the grader
    assigned = 0
    grader = None
    if is_ta:
        grader = models.User.objects.get(username=current_user.username)
        assigned = assignment.submission_set.filter(grader=grader).count()
    
    students = models.Group.objects.get(name="Students").user_set.count()

    # If user is a student, then get their submission
    student = None
    student_submission = None
    is_due = False
    submitted_graded = False
    submitted_ungraded = False
    submitted_early = False
    not_submitted_not_due = False
    not_submitted_past_due = False
    grade = 0
    if is_student:
        # Check due date 
        if assignment.deadline < timezone.now():
            is_due = True
        student = User.objects.get(username=current_user.username)
        try: 
            student_submission = assignment.submission_set.get(author=student)
            if is_due: 
                if student_submission.score != None:
                    submitted_graded = True
                    grade = (student_submission.score/assignment.points) * 100
                else: 
                    submitted_ungraded = True
            else: 
                submitted_early = True
        except Submission.DoesNotExist:
            student_submission = None
            if is_due: 
                not_submitted_past_due = True
            else: 
                not_submitted_not_due = True
        if request.method == "POST":
            submitted_file = request.FILES["file"]
            
            if student_submission:
                # Don't let them submit if past due
                if is_due:
                    return HttpResponseBadRequest
                # Check file size
                if (submitted_file.size > (64 * 1024 * 1024)):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "File size too large",
                    })
                # Check that file is a pdf
                if not (submitted_file.name.endswith(".pdf")):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "Only .pdf files accepted",
                    })
                elif not (next(submitted_file.chunks()).startswith(b'%PDF-')):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "Only .pdf files accepted",
                    })
                student_submission.file = submitted_file
                try: 
                    student_submission.full_clean()
                except ValidationError as e:
                    return e.message_dict
                student_submission.save()
            else: 
                if (submitted_file.size > (64 * 1024 * 1024)):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "File size too large",
                    })
                # Check that file is a pdf
                if not (submitted_file.name.endswith(".pdf")):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "Only .pdf files accepted",
                    })
                elif not (next(submitted_file.chunks()).startswith(b'%PDF-')):
                    return render(request, "assignment.html", {
                        "assignment": assignment,
                        "submissions": submissions,
                        "assigned": assigned,
                        "students": students,
                        "student_submission": student_submission, 
                        "is_student": is_student,
                        "is_ta": is_ta,
                        "is_administrator": is_administrator,
                        "graded": submitted_graded,
                        "ungraded":submitted_ungraded,
                        "early_submission":submitted_early,
                        "no_submission":not_submitted_not_due,
                        "missing":not_submitted_past_due,
                        "is_due": is_due,
                        "grade": grade,
                        "error" : "Only .pdf files accepted",
                    })

                student_submission = Submission(assignment=assignment, file=submitted_file, author=student, grader = pick_grader(assignment), score=None)
                student_submission.save()   
            return redirect(f"/{assignment_id}")
    
    return render(request, "assignment.html", {
        "assignment": assignment,
        "submissions": submissions,
        "assigned": assigned,
        "students": students,
        "student_submission": student_submission, 
        "is_student": is_student,
        "is_ta": is_ta,
        "is_administrator": is_administrator,
        "graded": submitted_graded,
        "ungraded":submitted_ungraded,
        "early_submission":submitted_early,
        "no_submission":not_submitted_not_due,
        "missing":not_submitted_past_due,
        "is_due": is_due,
        "grade": grade,
        "error": "",
    })

@login_required
def submissions(request, assignment_id):
    # Get currently logged in user and check for student, ta, and administrator values
    current_user = request.user
    anonymous_user = not current_user.is_authenticated
    # If the user is logged in, then check for their status
    if not anonymous_user:
        is_ta = current_user.groups.filter(name="Teaching Assistants").exists()
        is_administrator = current_user.is_superuser
    if not (is_ta or is_administrator):
        raise PermissionDenied 

    error_dictionary = {}
    if request.method == "POST":
        returned_data = iterate_request(request, assignment_id, current_user)
        error_dictionary = returned_data[0]
        errors = returned_data[1]
        if not errors:
            return redirect(f"/{assignment_id}/submissions")  
        else:       
            assignment = get_object_or_404(models.Assignment, id=assignment_id)
            points = assignment.points
            grader = models.User.objects.get(username=current_user.username)

            # If it is a ta, only show submissions assigned to them. If admin, show all submissions
            if is_ta: 
                submissions = assignment.submission_set.filter(grader=grader)
            if is_administrator:
                submissions = assignment.submission_set

            # order submissions by author name
            submissions = submissions.order_by("author__username")
            students = []
            for submission in submissions: 
                students.append(submission.author.get_full_name())
            data = zip(submissions, students, error_dictionary.values())
            return render(request, "submissions.html", {
                "data": data,
                "assignment": assignment,
                "points": points,
            })
    else: 
        assignment = get_object_or_404(models.Assignment, id=assignment_id)
        points = assignment.points
        grader = models.User.objects.get(username=current_user.username)
        # If it is a ta, only show submissions assigned to them. If admin, show all submissions
        if is_ta: 
            submissions = assignment.submission_set.filter(grader=grader)
        if is_administrator:
            submissions = assignment.submission_set
        # order submissions by author name
        submissions = submissions.order_by("author__username")
        students = []
        for submission in submissions: 
            if not submission.id in error_dictionary:
                error_dictionary[submission.id] = []
            students.append(submission.author.get_full_name())
        data = zip(submissions, students, error_dictionary.values())
        print(error_dictionary)
        return render(request, "submissions.html", {
            "data": data,
            "assignment": assignment,
            "points": points,
        })

@login_required
def profile(request):
     # Get currently logged in user and check for student, ta, and administrator values
    current_user = request.user
    anonymous_user = not current_user.is_authenticated
    # If the user is logged in, then check for their status
    if not anonymous_user:
        is_student = current_user.groups.filter(name="Students").exists()
        is_ta = current_user.groups.filter(name="Teaching Assistants").exists()
        is_administrator = current_user.is_superuser

    assignments = models.Assignment.objects.all()

    # Create empty lists to allow for different user types 
    combined_data = None
    grades = None
    final_grade = 0
    total_submissions = []
    graded_submissions = []

    # If is an admin, show graded submissions out of all submissions
    if is_administrator:
        for assignment in assignments:
            total_submissions.append(assignment.submission_set.count())
            graded_submissions.append(assignment.submission_set.filter(score__isnull=False).count())
        combined_data = zip(assignments, graded_submissions, total_submissions)

    # If is a student, show grades at the end of the table row
    if is_student:
        student = User.objects.get(username=current_user.username)
        grades = []
        available_grade_points = 0
        earned_grade_points = 0
        for assignment in assignments:
            # If deadline has not passed, mark Not Due
            if assignment.deadline > timezone.now():
                grades.append("Not Due")
                continue
            # If deadline has passed, check for a submission
            try: 
                submission = assignment.submission_set.get(author=student)
            # If missing, then it contributes to available points and nothing to earned points
            except Submission.DoesNotExist:
                grades.append("Missing")
                available_grade_points += assignment.weight
                continue
            submission_grade = submission.score
            # Graded, so contributes to total grade
            if submission_grade:
                grades.append(str((submission.score/assignment.points)*100)+"%")
                available_grade_points += assignment.weight
                earned_grade_points += (submission.score/assignment.points) *  assignment.weight
                continue
            else: 
                grades.append("Ungraded")
                continue
        final_grade = round((earned_grade_points / available_grade_points) * 100, 1)
        combined_data = zip(assignments, grades)

    # If user is a ta, then show all assignments and how many are graded out of the total need to be graded
    if is_ta:
        assigned_list = []
        graded_list = []
        for assignment in assignments: 
            grader = models.User.objects.get(username=current_user.username)
            assigned_list.append(assignment.submission_set.filter(grader=grader).count())
            graded_list.append(assignment.submission_set.filter(grader=grader).filter(score__isnull=False).count())
        # Zip data to easily loop over them all at the same time
        combined_data = zip(assignments, graded_list, assigned_list)

    return render(request, "profile.html", {
        "data": combined_data,
        "user": current_user, 
        "grades": grades,
        "final_grade": final_grade,
        "is_ta": is_ta,
        "is_student": is_student,
        "is_admin": is_administrator,
    })

def login_form(request):
    next_page = request.GET.get("next", "/profile/")
    if request.method == "POST":
        next_page = request.POST["next"]
        username = request.POST.get("username", "")
        password = request.POST.get("password","")
        user = authenticate(username=username, password=password)
        if user is not None: 
            login(request, user)
            if url_has_allowed_host_and_scheme(next_page, None):
                return redirect(request.POST.get("next", "/profile/"))  
        else: 
            return render(request, "login.html", {
                "next": next_page,
                "error": "Username and password do not match",
            })   
    
        
    return render(request, "login.html", {
        "next": next_page,
    })

def logout_form(request):
    logout(request)
    return redirect("/profile/login/")

def iterate_request(request, assignment_id, user):
    error_dictionary = {}
    errors = False
    assignment = get_object_or_404(models.Assignment, id=assignment_id)
    submission_set = []
    for submission in request.POST:
        if submission.startswith("grade-"):
            id = int(submission.removeprefix("grade-"))
            error_dictionary[id] = []
            # Check that the submission id exists
            try: 
                Submission.objects.get(id=id)
            except Submission.DoesNotExist:
                errors = True
                if not id in error_dictionary:
                    error_dictionary[id] = []
                error_dictionary[id].append("Invalid Submission ID. This submission does not exist")
                continue
            # Check that the submission is for the correct assignment
            try:
                submission_object = assignment.submission_set.get(id=id)
            except Submission.DoesNotExist: 
                errors = True
                if not id in error_dictionary:
                    error_dictionary[id] = []
                error_dictionary[id].append("Incorrect Assignment Submission. This submission is linked to the wrong assignment")
                continue
            
            score = request.POST[submission]
            if len(score)==0:
                submission_object.score = None
                submission_set.append(submission_object)
            else: 
                # Check that the submission score is a number
                try: 
                    float_score = float(score)
                    submission_object.change_grade(user, float_score)
                    # Check that the submission score is within 0 to max assignment points
                    if submission_object.score < 0 or submission_object.score > assignment.points:
                        if not id in error_dictionary:
                            error_dictionary[id] = []
                        error_dictionary[id].append("Invalid Score Value. Please input a score greater than zero and less than or equal to the assignment's point value")
                        errors = True
                        continue
                    else: 
                        submission_set.append(submission_object)
                except ValueError:
                    errors = True
                    if not id in error_dictionary:
                        error_dictionary[id] = []
                    error_dictionary[id].append("Invalid Score Type. Please input a number for the submission score.")
                    continue
                            
    models.Submission.objects.bulk_update(submission_set, ["score"])
    return [error_dictionary, errors]

@login_required
def show_upload(request, filename):
    submission = get_object_or_404(models.Submission, file=filename)
    user = request.user
    file = submission.view_submission(user)
    # Check that file is a pdf
    if not (file.name.endswith(".pdf")):
        raise Http404
    elif not (next(file.chunks()).startswith(b'%PDF-')):
        raise Http404
    response = HttpResponse(file.open())
    response.headers["Content-Type"] = "application/pdf"
    response.headers["Content-Disposition"] = "attachment; filename=\"" + file.name + "\""
    return response

def pick_grader(Assignment):
    ta_group = models.Group.objects.get(name="Teaching Assistants").user_set
    ta_group_list = ta_group.annotate(total_assigned=Count("graded_set", filter=Q(graded_set__assignment=Assignment))).order_by("total_assigned")
    return ta_group_list[0]

    
