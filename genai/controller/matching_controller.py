from flask import Blueprint, request, jsonify
from matching_engine import MatchingEngine

matching_bp = Blueprint('matching', __name__)
engine = MatchingEngine()

@matching_bp.route('/match', methods=['POST'])
def match():
    data = request.get_json()
    user_profile = data.get('user_profile', '')
    candidates = data.get('candidates', [])
    scores = engine.match(user_profile, candidates)
    return jsonify({'scores': scores})
